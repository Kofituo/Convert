package com.otuolabs.unitconverter

import android.animation.ObjectAnimator
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.LayerDrawable
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.text.*
import android.text.style.StyleSpan
import android.util.ArrayMap
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.edit
import androidx.core.content.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionManager
import com.google.android.gms.ads.AdRequest
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.otuolabs.unitconverter.AdditionItems.Author
import com.otuolabs.unitconverter.AdditionItems.FavouritesCalledIt
import com.otuolabs.unitconverter.AdditionItems.SearchActivityCalledIt
import com.otuolabs.unitconverter.AdditionItems.SearchActivityExtra
import com.otuolabs.unitconverter.AdditionItems.TextMessage
import com.otuolabs.unitconverter.AdditionItems.ToolbarColor
import com.otuolabs.unitconverter.AdditionItems.ViewIdMessage
import com.otuolabs.unitconverter.AdditionItems.pkgName
import com.otuolabs.unitconverter.MainActivity.Companion.restoreUiModeOnResume
import com.otuolabs.unitconverter.Utils.dpToInt
import com.otuolabs.unitconverter.Utils.filters
import com.otuolabs.unitconverter.Utils.hoursToMilliSeconds
import com.otuolabs.unitconverter.Utils.insertCommas
import com.otuolabs.unitconverter.Utils.lengthFilter
import com.otuolabs.unitconverter.Utils.minusSign
import com.otuolabs.unitconverter.Utils.removeCommas
import com.otuolabs.unitconverter.Utils.temperatureFilters
import com.otuolabs.unitconverter.ads.AdsManager
import com.otuolabs.unitconverter.builders.*
import com.otuolabs.unitconverter.functions.Currency
import com.otuolabs.unitconverter.miscellaneous.*
import com.otuolabs.unitconverter.miscellaneous.ResetAfterNGets.Companion.resetAfter2Gets
import com.otuolabs.unitconverter.miscellaneous.ResetAfterNGets.Companion.resetAfterGet
import com.otuolabs.unitconverter.networks.DownloadCallback
import com.otuolabs.unitconverter.networks.NetworkFragment
import com.otuolabs.unitconverter.networks.Statuses
import com.otuolabs.unitconverter.networks.Token
import com.otuolabs.unitconverter.subclasses.Constraints
import com.otuolabs.unitconverter.subclasses.ConvertViewModel
import com.otuolabs.unitconverter.subclasses.DisableEditText
import com.otuolabs.unitconverter.subclasses.Positions
import kotlinx.android.synthetic.main.activity_convert.*
import kotlinx.coroutines.*
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.parseMap
import kotlinx.serialization.stringify
import java.io.Serializable
import java.net.SocketTimeoutException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

@Suppress("DEPRECATION")
@OptIn(ImplicitReflectionSerializer::class)
class ConvertActivity : AppCompatActivity(), ConvertFragment.ConvertDialogInterface,
        DownloadCallback<String>, CoroutineScope by MainScope(), PreferenceFragment.PreferenceFragment {

    private var swap = false
    private var randomColor = -1
    private var viewId = -1
    private lateinit var dialog: ConvertFragment
    private var isPrefix = false
    private val bundle = Bundle()
    private lateinit var viewName: String
    private lateinit var function: (Positions) -> String
    private inline val isTemperature get() = viewId == R.id.Temperature
    private lateinit var viewModel: ConvertViewModel
    private inline val isCurrency get() = viewId == R.id.Currency
    private lateinit var networkFragment: NetworkFragment
    private val isNumberBase get() = viewId == R.id.number_base
    private val activityCallbacks: ActivityCallbacks? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        //restoreUiMode()
        super.onCreate(savedInstanceState)
        if (intent.getBooleanExtra(SearchActivityCalledIt, false))
            overridePendingTransition(R.anim.scale_out, android.R.anim.fade_out)
        setContentView(R.layout.activity_convert)
        setSupportActionBar(convert_app_bar as Toolbar?)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
        val isRTL =
                TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == View.LAYOUT_DIRECTION_RTL

        if (!isRTL) {
            bottom_button.setLeftPadding(this, -3) //converts it to dp
            top_button.setLeftPadding(this, -3)
        }
        dialog = ConvertFragment()
        // for setting the text
        intent {
            getStringExtra(TextMessage)?.also {
                bundle.putString("viewName", it)
                convert_header?.text = it
                app_bar_text.text = it
                viewName = it
            }
            viewId = getIntExtra(ViewIdMessage, -1).apply {
                isPrefix = equals(R.id.prefixes)
                bundle.putInt("viewId", this)
            }
        }
        getLastConversions()

        if (!isNumberBase) {
            firstEditText.setRawInputType(Configuration.KEYBOARD_12KEY)
            secondEditText.setRawInputType(Configuration.KEYBOARD_12KEY)
        }
        viewModel = viewModel {
            settingColours(randomInt)
            randomInt = randomColor
        }
        if (!getCurrencyData())
            function = InitializeFunction(viewId).function
        getTextWhileTyping()
        top_button.setOnClickListener {
            if (!dialog.isAdded)
                dialog {
                    bundle.putInt("whichButton", it.id)
                    viewModel.whichButton = it.id
                    arguments = bundle
                    show(supportFragmentManager, "dialog")
                }
        }
        bottom_button.setOnClickListener {
            if (!dialog.isAdded)
                dialog {
                    bundle.putInt("whichButton", it.id)
                    viewModel.whichButton = it.id
                    arguments = bundle
                    show(supportFragmentManager, "dialog")
                }
        }
        selectFirstBox()
        AdsManager.initializeBannerAd(convert_parent as ConstraintLayout)
        onCreateCalled = true
    }

    private fun getCurrencyData(): Boolean {
        if (isCurrency) {
            //update the map
            if (firstTime.isNull() ||
                    (System.currentTimeMillis() - firstTime!!) > hoursToMilliSeconds(22) // 22hours
                    || currenciesList.isNullOrEmpty()
            ) {
                setNetworkListener()
                networkFragment =
                        NetworkFragment
                                .createFragment(supportFragmentManager) {
                                    urls = urlArray
                                }
                launch {
                    delay(400)
                    networkFragment.startDownload() //it's too quick
                }
            }
            if (!currenciesList.isNullOrEmpty()) //use old map for now
                bundle.putSerializable("for_currency", currenciesList as Serializable)
            currencyConversion()
        }
        return isCurrency
    }

    private fun selectFirstBox() {
        intent {
            val searchData =
                    getSerializableExtra(SearchActivityExtra)
            if (searchData.isNotNull()) {
                searchData as RecyclerDataClass
                firstBox.hint = searchData.topText
                val text = searchData.bottomText
                topTextView.text =
                        if (getBooleanExtra("IsSpans", false)) {
                            removeExtra("IsSpans")
                            SpannableString(text).apply {
                                setSpan(StyleSpan(Typeface.BOLD), 0, length, 0)
                            }
                        } else text
                positionArray["topPosition"] = searchData.id
                firstBox.requestFocus()
                val sharedPreferences by sharedPreference {
                    pkgName + viewName
                }
                //for the convert fragment
                sharedPreferences.edit {
                    put<Int> {
                        key = "topButton"
                        value = searchData.id
                    }
                }
                removeExtra(SearchActivityExtra)//to reset it in case of rotation
            }
        }
    }

    private var onCreateCalled by resetAfterGet(initialValue = true, resetValue = false)

    override fun onResume() {
        //restoreUiMode()
        super.onResume()
        restoreUiModeOnResume()
        AdsManager.bannerAdCallbackListener.onResume()
        if (onCreateCalled) {
            if (isNumberBase) {
                removeFilters(firstEditText)
                removeFilters(secondEditText)
            } else {
                setFilters(firstEditText)
                setFilters(secondEditText)
            }
            resetEditTexts()
        }
    }

    private inline fun dialog(block: ConvertFragment.() -> Unit) =
            dialog.apply(block)

    private inline fun intent(block: Intent.() -> Unit) = intent.apply(block)

    private inline fun viewModel(block: ConvertViewModel.() -> Unit) =
            ViewModelProvider(this)[ConvertViewModel::class.java] // for the view model
                    .apply(block)

    private lateinit var firstWatcher: CommonWatcher
    private lateinit var secondCommonWatcher: CommonWatcher

    private fun setFilters(editText: TextInputEditText) {
        editText.filters =
                when {
                    isTemperature -> temperatureFilters(editText)
                    isNumberBase -> arrayOf(lengthFilter())
                    else -> filters(editText)
                }
    }

    override fun texts(text: CharSequence, unit: CharSequence) {
        val whichButton = viewModel.whichButton
        if (whichButton == R.id.top_button) {
            if (firstBox.hint != text) {
                firstBox.hint = text
                topTextView.apply {
                    this.text = unit
                    layoutParams = layoutParams {
                        width = ViewGroup.LayoutParams.WRAP_CONTENT
                        height = ViewGroup.LayoutParams.WRAP_CONTENT
                    }
                }
            }
        } else {
            if (secondBox.hint != text) {
                secondBox.hint = text
                bottomTextView.apply {
                    this.text = unit
                    layoutParams = layoutParams {
                        width = ViewGroup.LayoutParams.WRAP_CONTENT
                        height = ViewGroup.LayoutParams.WRAP_CONTENT
                    }
                }
            }
        }
    }

    private val positionArray = ArrayMap<String, Int>(2)

    override fun getOtherValues(position: Int, positionKey: String) {
        val initialMap = ArrayMap(positionArray)
        positionArray[positionKey] = position
        if (initialMap == positionArray) return
        val editTextInFocus: EditText
        val editTextNotInFocus: EditText
        val editTextInFocusBoolean: Boolean
        val editTextNotInFocusBoolean: Boolean
        val editTextInFocusWatcher: CommonWatcher
        val editTextNotInFocusWatcher: CommonWatcher
        //initialize variables
        if (firstEditText.isFocused) {
            editTextInFocus = firstEditText
            editTextInFocus.text?.apply { if (isEmpty()) return }
            editTextNotInFocus = secondEditText
            editTextInFocusBoolean = firstBoolean
            editTextNotInFocusBoolean = secondBoolean
            editTextInFocusWatcher = firstWatcher
            editTextNotInFocusWatcher = secondCommonWatcher
        } else {
            editTextInFocus = secondEditText
            editTextInFocus.text?.apply { if (isEmpty()) return }
            editTextNotInFocus = firstEditText
            editTextInFocusBoolean = secondBoolean
            editTextNotInFocusBoolean = firstBoolean
            editTextInFocusWatcher = secondCommonWatcher
            editTextNotInFocusWatcher = firstWatcher
        }
        val initialReverse = if (reverse) 1 else 0
        // keep the edit text in focus text's constant

        editTextInFocus.apply {
            //initial text
            val initialText = Editable.Factory.getInstance().newEditable(text)
            text = null
            if (!editTextInFocusBoolean) {
                editTextNotInFocus.removeTextChangedListener(editTextNotInFocusWatcher)
                addTextChangedListener(editTextInFocusWatcher)
                reverse = this != firstEditText
            }
            text = initialText
            //settings thing back
            if (editTextNotInFocusBoolean)
                editTextNotInFocus.addTextChangedListener(editTextNotInFocusWatcher)
            if (!editTextInFocusBoolean) removeTextChangedListener(editTextInFocusWatcher)
            reverse = initialReverse != 0
        }
    }

    private var topPosition: Int = -1
    private var bottomPosition: Int = -1
    private fun getPositions(): Boolean? {
        if (positionArray.valueAt(0) == -1 ||
                positionArray.valueAt(1) == -1
        ) return false

        topPosition =
                if (reverse) positionArray["bottomPosition"]!! else positionArray["topPosition"]!!
        bottomPosition =
                if (reverse) positionArray["topPosition"]!! else positionArray["bottomPosition"]!!
        if (topPosition == bottomPosition) return null
        return true
    }

    private var reverse = false

    /**
     * Assuming someone opens it 3 hours later and starts typing before it refreshes
     * [currencyRates] would be updated and so should [currencyRatesEnumeration]
     * */
    private val currencyRatesEnumeration = MutableLazy.resettableLazy {
        var index = 0
        currencyRates?.run {
            val map = LinkedHashMap<Int, String>(size)
            forEach {
                map[index++] = it.key
            }
            map
        }
    }

    private fun currencyConversion() {
        val enumeratedMap by currencyRatesEnumeration // so i  can reset the value
        function = {
            Currency.buildConversions {
                positions = it
                ratesMap = currencyRates
                this.enumerationRates = enumeratedMap
            }.getText()
        }
    }

    private var preferenceFragment: PreferenceFragment? = null

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        @Suppress("EXPERIMENTAL_API_USAGE")
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.swap -> {
                swap()
                true
            }
            R.id.prefixes ->
                buildIntent<ConvertActivity> {
                    putExtra(TextMessage, "Prefix")
                    putExtra(ViewIdMessage, R.id.prefixes)
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(this)
                }
            R.id.preferences -> {
                viewModel.clearForPreferences()
                if (preferenceFragment.isNull()) {
                    PreferenceFragment().apply {
                        arguments = bundle
                        preferenceFragment = this
                    }
                }
                preferenceFragment?.let {
                    if (!it.isAdded)
                        it.show(supportFragmentManager, "PREFERENCES")
                }
                true
            }
            R.id.search_button -> {
                onSearchRequested()
                true
            }
            R.id.feedback -> MainActivity.sendFeedback(this)

            R.id.settings -> buildIntent<SettingsActivity> {
                startActivity(this)
                finish()
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSearchRequested(): Boolean {
        AdsManager.showInterstitialAd()
        buildIntent<SearchActivity> {
            putExtra(ToolbarColor, randomColor)
            startActivity(this)
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.convert_menu, menu)
        val favoritesCalledIt = intent.getBooleanExtra(FavouritesCalledIt, false)
        if (favoritesCalledIt) menu?.removeItem(R.id.favourite)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (isPrefix) menu?.removeItem(R.id.prefixes)
        return super.onPrepareOptionsMenu(menu)
    }

    private fun swap() {
        val firstBox = if (swap) R.id.secondBox else R.id.firstBox
        val secondBox = if (swap) R.id.firstBox else R.id.secondBox
        val topButton = if (swap) R.id.bottom_button else R.id.top_button
        val bottomButton = if (swap) R.id.top_button else R.id.bottom_button
        buildConstraintSet {
            this clones convert_inner
            constraint(firstBox) {
                val value = 40.dpToInt(this@ConvertActivity)
                margin(value) and it topToBottomOf secondBox
                this margin 0 and it bottomToBottomOf ConstraintSet.PARENT_ID
            }
            // for the second box
            constraint(secondBox) {
                it clear Constraints.BOTTOM
                this margin 0 and it topToTopOf R.id.topGuide
            }
            // for the top button
            constraint(topButton) {
                it topToTopOf firstBox
                it bottomToBottomOf firstBox
                it startToEndOf firstBox
            }
            //for the down button
            constraint(bottomButton) {
                it topToTopOf R.id.topGuide
                it bottomToBottomOf secondBox
                it startToEndOf secondBox
            }
            TransitionManager.beginDelayedTransition(convert_inner)
            this appliesTo convert_inner
        }
        swap = !swap
    }

    private fun settingColours(colorInt: Int) {
        //randomly get colour
        randomColor = if (colorInt == 0) Color.parseColor(colors.random()) else colorInt
        window {
            statusBarColor = randomColor
            decorView.apply {
                post {
                    if (Build.VERSION.SDK_INT > 22) systemUiVisibility =
                            systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            }
        }
        randomColor.also {
            setCornerColors(it)
            background_view.setBackgroundColor(it)
            firstBox.boxStrokeColor = it
            secondBox.boxStrokeColor = it
            ColorStateList.valueOf(12)
            val colorStateList = ColorStateList.valueOf(it)
            top_button.apply {
                iconTint = colorStateList
                rippleColor = colorStateList
            }
            bottom_button.apply {
                iconTint = colorStateList
                rippleColor = colorStateList
            }
        }
    }

    private fun setCornerColors(colorInt: Int) {
        (right_corner.background as LayerDrawable)
                .findDrawableByLayerId(R.id.top_color)
                .setTint(colorInt)
        (left_corner.background as LayerDrawable)
                .findDrawableByLayerId(R.id.top_color)
                .setTint(colorInt)
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
        AdsManager.bannerAdCallbackListener.onDestroy()
    }

    private inline fun window(block: Window.() -> Unit) = window?.apply(block)

    //could have used reflection
    private inline fun callBack(f: (Positions) -> String, x: String): String {
        return if (x.isEmpty()) ""
        else {
            val getPosition = getPositions()
            when {
                getPosition.isNull() && isNumberBase ->
                    f(Positions(topPosition, bottomPosition, x))
                getPosition.isNull() -> x.insertCommas()
                !getPosition -> ""
                else -> f(Positions(topPosition, bottomPosition, x))
            }
        }
    }

    private fun removeFilters(editText: EditText) {
        editText.filters = arrayOf(lengthFilter())
    }

    inner class CommonWatcher(
            private val editText: EditText,
            private val secondEditText: TextInputEditText
    ) :
            SeparateThousands(editText) {
        override fun afterTextChanged(s: Editable?) {
            if (s.isNull() || secondEditText.isFocused) return
            if (getNumberBases(s, editText, secondEditText, this)) return
            super.afterTextChanged(s)
            s.apply {
                if (length == 1 && get(0) == minusSign) {
                    secondEditText.text = null // to prevent Unparseable number "-"error
                    return
                }
                removeCommas(Utils.decimalSeparator)?.also {
                    setTexts(it, secondEditText)
                }
            }
        }
    }

    private fun setTexts(string: String, secondEditText: TextInputEditText): String {
        secondEditText.apply {
            removeFilters(this)
            val result = callBack(function, string)
            this as DisableEditText
            shouldDisable(Utils.decimalFormatSymbols?.exponentSeparator!! in result)
            setText(result)
            setFilters(this)
            return result
        }
    }

    private fun getNumberBases(
            currentText: CharSequence,
            currentEditText: EditText,
            secondEditText: TextInputEditText,
            currentEditTextWatcher: TextWatcher
    ): Boolean {
        if (isNumberBase) {
            val result = setTexts(currentText.toString(), secondEditText)
            currentEditText.removeTextChangedListener(currentEditTextWatcher)
            if (topPosition != -1 && bottomPosition != -1 && result.isEmpty()) {
                //means wrong input string
                ///val text = currentEditText.text ?: return isNumberBase
                val redText = SpannableString(currentText).apply {
                    setSpan(RedUnderline(resources), 0, currentText.length, 0)
                }
                setTextWithCursorPosition(currentEditText, redText)
            } else //remove the spans since it's correct
                if (currentText is Spanned)
                    setTextWithCursorPosition(currentEditText, currentText.toString())
            currentEditText.addTextChangedListener(currentEditTextWatcher)
        }
        return isNumberBase
    }

    private fun setTextWithCursorPosition(editText: EditText, text: CharSequence) {
        editText.apply {
            val cursorPosition = selectionEnd
            setText(text)
            setSelection(cursorPosition)
        }
    }

    private var firstBoolean = false
    private var secondBoolean = false
    private fun getTextWhileTyping() {
        firstEditText.apply {
            firstWatcher = CommonWatcher(this, secondEditText)
            setOnFocusChangeListener { _, hasFocus ->
                firstBoolean = hasFocus
                if (hasFocus) {
                    addTextChangedListener(firstWatcher)
                    secondEditText.removeTextChangedListener(secondCommonWatcher)
                    reverse = false
                }
            }
        }
        secondEditText.apply {
            secondCommonWatcher = CommonWatcher(this, firstEditText)
            setOnFocusChangeListener { _, hasFocus ->
                secondBoolean = hasFocus
                //Log.e("4", "4 firstHasFocus $firstBoolean  secondHasFocus $secondBoolean")
                if (hasFocus) {
                    addTextChangedListener(secondCommonWatcher)
                    firstEditText.removeTextChangedListener(firstWatcher)
                    reverse = true
                }
            }
        }
    }

    private val sharedPreferences by sharedPreference {
        buildString {
            append(pkgName)
            append(viewName)
            append(Author) // to prevent name clashes with the fragment
        }
    }

    private var currenciesList: MutableList<RecyclerDataClass>? = null
    private var currencyRates: Map<String, String>? = null
    private var currencyLoadedBefore: Boolean? = null
    private var firstTime: Long? = null

    private fun getLastConversions() {
        sharedPreferences {
            get<String?>("topTextViewText") {
                topTextView.text =
                        if (get("topIsSpans"))
                            if (Build.VERSION.SDK_INT < 24) Html.fromHtml(this).trim()
                            else Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT).trim()
                        else this
            }
            get<String?>("bottomTextViewText") {
                bottomTextView.text =
                        if (get("bottomIsSpans"))
                            if (Build.VERSION.SDK_INT < 24) Html.fromHtml(this).trim()
                            else Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT).trim()
                        else this
            }
            secondBox.hint =
                    get<String?>("bottomEditTextText") ?: resources.getString(R.string.select_unit)
            firstBox.hint =
                    get<String?>("topEditTextText") ?: resources.getString(R.string.select_unit)

            //get last positions
            val topPosition = get<Int>("topPosition")
            val bottomPosition = get<Int>("downPosition")
            positionArray.apply {
                put("topPosition", topPosition)
                put("bottomPosition", bottomPosition)
            }
            if (isCurrency) {
                //get currency preferences
                get<String?>("list_of_currencies") {
                    if (this.hasValue()) {
                        //means loaded before
                        currencyLoadedBefore = true
                        currenciesList = getCurrencyList(this)
                    } else
                        currencyLoadedBefore = false
                }
                get<Long>("previous_time") {
                    if (this != -1L)
                        firstTime = this
                }
                get<String?>("values_for_conversion") {
                    if (this.hasValue())
                        currencyRates = getRatesList(this)
                }
            }
            Utils.isEngineering = get("isEngineering")
            get<Int>("sliderValue") {
                if (this != -1)
                    sliderValue = this
            }
            get<String?>("preferences_selections") {
                if (this.hasValue())
                    preferencesSelected.putAll {
                        Json.parseMap(this)
                    }
                else preferencesSelected.apply {
                    put(0, 0)
                    put(1, 3)
                    put(2, 7)
                    put(3, 11)
                }
                applyChanges() //build the decimal format
            }
            Utils.numberOfDecimalPlace = sliderValue
        }
    }

    companion object {
        @OptIn(ImplicitReflectionSerializer::class)
        fun getCurrencyList(string: String): MutableList<RecyclerDataClass> {
            Json.parseMap<String, String>(string).apply {
                val list = ArrayList<RecyclerDataClass>(size)
                var start = 0
                forEach {
                    list.add {
                        RecyclerDataClass(it.key, it.value, start++)
                    }
                }
                return list
            }
        }

        val adRequest: AdRequest
                by lazy {
                    AdRequest
                            .Builder()
                            .addKeyword("Insurance")
                            .addKeyword("health")
                            .addKeyword("banking")
                            .addKeyword("business")
                            .addKeyword("internet")
                            .addKeyword("wellness")
                            .addKeyword("Marketing and Advertising")
                            .addKeyword("legal")
                            .addKeyword("forex")
                            .addKeyword("online education")
                            .addKeyword("education")
                            .addKeyword("home")
                            .addKeyword("telecom")
                            .addKeyword("automobile dealership")
                            .build()
                }
    }

    private fun getRatesList(string: String) = Json.parseMap<String, String>(string)

    private fun saveData() {
        editPreferences {
            put<String> {
                key = "topEditTextText"
                value = firstBox.hint.toString().let {
                    if (it != resources.getString(R.string.select_unit)) it
                    else null
                }
            }
            val topTextViewText = topTextView.text
            put<String> {
                key = "topTextViewText"
                value = topTextViewText.let {
                    if (it is SpannedString) {
                        if (Build.VERSION.SDK_INT < 24) Html.toHtml(it)
                        else
                            Html.toHtml(it, Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL)
                    } else it.toString()
                }
            }
            put<Boolean> {
                key = "topIsSpans"
                value = topTextViewText is SpannedString
            }

            val bottomTextViewText = bottomTextView.text
            put<String> {
                key = "bottomTextViewText"
                value = bottomTextViewText.let {
                    if (it is SpannedString) {
                        if (Build.VERSION.SDK_INT < 24) Html.toHtml(it)
                        else
                            Html.toHtml(it, Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL)
                    } else it.toString()
                }
            }
            put<Boolean> {
                key = "bottomIsSpans"
                value = bottomTextViewText is SpannedString
            }
            put<String> {
                key = "bottomEditTextText"
                value = secondBox.hint.toString().let {
                    if (it != resources.getString(R.string.select_unit)) it
                    else null
                }
            }
            put<Int> {
                key = "topPosition"
                value = positionArray["topPosition"]!!
            }
            put<Int> {
                key = "downPosition"
                value = positionArray["bottomPosition"]!!
            }
            put<String> {
                key = "preferences_selections"
                value = Json.stringify(preferencesSelected)
            }
            put<Int> {
                key = "sliderValue"
                value = sliderValue
            }
            put<Boolean> {
                key = "isEngineering"
                value = Utils.isEngineering
            }
            apply()
        }
    }

    private inline fun sharedPreferences(block: SharedPreferences.() -> Unit): SharedPreferences =
            sharedPreferences(sharedPreferences, block)

    private inline fun editPreferences(block: SharedPreferences.Editor.() -> Unit) =
            editPreferences(sharedPreferences, block)

    override fun onPause() {
        super.onPause()
        AdsManager.bannerAdCallbackListener.onPause()
        if (isFinishing && !AdsManager.showInterstitialAd())  //don't play animation if the add is about to show
            if (intent.getBooleanExtra(SearchActivityCalledIt, false))
                ScaleTransition.mergeAnimators(
                        ObjectAnimator.ofFloat(convert_parent, View.SCALE_X, 0.3f),
                        ObjectAnimator.ofFloat(convert_parent, View.SCALE_Y, 0.3f)
                ).start()
        saveData()
    }

    // for currencies
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private var retry: Boolean? = null

    private fun refreshEverything() {
        positionArray["bottomPosition"] = -1
        positionArray["topPosition"] = -1
        getString(R.string.select_unit).apply {
            firstBox.hint = this
            secondBox.hint = this
        }
        null.apply {
            firstEditText.text = this
            secondEditText.text = this
            topTextView.text = this
            bottomTextView.text = this
        }
    }

    private val urlArray by lazy(LazyThreadSafetyMode.NONE) {
        buildMutableList<String>(2) {
            add {
                "${Token.Repository}/currency_conversions/contents/values.json"
            }
            add {
                "${Token.Repository}/currency_conversions/contents/currency.json"
            }
        }
    }
    var networkIsAvailable: Boolean? = null

    /**show when null*/
    private var snackBar: Snackbar? = null

    private var shouldShowSnack: Boolean? by resetAfter2Gets(null, null)

    override fun updateFromDownload(url: String?, result: String?) {
        if (url.isNotNull() && result.isNotNull()) {
            val preferenceKey: String
            when (url) {
                urlArray[0] -> {
                    preferenceKey = "values_for_conversion" // values.json
                    getRates(result)
                }
                urlArray[1] -> {
                    preferenceKey = "list_of_currencies" // currency.json
                    getCurrencies(result)
                }
                else -> TODO()
            }
            editPreferences {
                put<String> {
                    key = preferenceKey
                    value = result
                }
                if (retry == false) //so it would only put after the first one has loaded
                    put<Long> {
                        key = "previous_time"
                        value = System.currentTimeMillis()
                    }
                apply()
            }
            retry = false
        } else {
            launch {
                retry = true
                delay(1000)
                showSnack {
                    view = convert_parent
                    resId = R.string.no_connection
                    duration = Snackbar.LENGTH_LONG
                    setAction {
                        resId = R.string.retry
                        listener = View.OnClickListener {
                            networkFragment.startDownload()
                        }
                    }
                }
            }
        }
    }

    private fun getRates(result: String) {
        currencyRates = getRatesList(result)
        currencyRatesEnumeration.reset()
        if (shouldShowSnack != false) {
            snackBar = showSnack {
                view = convert_parent
                resId = if (snackBar.isNull())
                    R.string.rates_success
                else R.string.currency_and_rates_success
                duration = Snackbar.LENGTH_SHORT
                callback =
                        object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                            override fun onDismissed(
                                    transientBottomBar: Snackbar?,
                                    event: Int
                            ) {
                                snackBar = null
                            }
                        }
            }
        }
    }

    private fun getCurrencies(result: String) {
        /**
         * Assuming for some reason the currency list changes e.g new currencies have
         * been added, everything should be reset
         * */
        currenciesList = getCurrenciesFromDownload(result)
        bundle.putSerializable("for_currency", currenciesList as Serializable)
        if (shouldShowSnack != false) {
            snackBar = showSnack {
                view = convert_parent
                resId =
                        if (snackBar.isNull()) R.string.currency_success
                        else R.string.currency_and_rates_success
                duration = Snackbar.LENGTH_SHORT
                callback =
                        object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                            override fun onDismissed(
                                    transientBottomBar: Snackbar?,
                                    event: Int
                            ) {
                                snackBar = null
                            }
                        }
            }
        }
    }

    private fun getCurrenciesFromDownload(result: String) =
            getCurrencyList(result).apply {
                //execute only when it isn't null
                currenciesList?.let { list ->
                    val shouldClear =
                            //quickly check the size
                            if (list.size != size) true
                            else {
                                var index = 0
                                //if there's a difference in  currency
                                !list.all {
                                    it.topText == this[index++].topText //compare only quantity to be fast
                                }
                            }
                    //Log.e("should", "clear $shouldClear")
                    if (shouldClear) {
                        refreshEverything()
                        //clear saved preferences
                        val sharedPreferences by sharedPreference {
                            pkgName + "Currency"
                        }
                        sharedPreferences.edit { clear() }
                        showSnack {
                            view = convert_parent
                            resId = R.string.currency_reset
                            duration = Snackbar.LENGTH_LONG
                        }
                        shouldShowSnack = false
                    }
                }
            }

    private fun setNetworkListener() {
        if (!::networkCallback.isInitialized) {
            getSystemService<ConnectivityManager>()?.apply {
                networkCallback = object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        networkIsAvailable = true
                        if (retry == true)
                            networkFragment.startDownload()
                    }

                    override fun onLost(network: Network) {
                        networkIsAvailable = false
                    }
                }
                registerNetworkCallback(NetworkRequest.Builder().build(), networkCallback)
            }
        }
    }

    override fun networkAvailable(): Boolean? = networkIsAvailable //shouldn't be null though

    override fun onProgressUpdate(progressCode: Int) {
        when (progressCode) {
            Statuses.CONNECT_SUCCESS -> {
                //show getting values
                showSnack {
                    view = convert_parent
                    resId =
                            if (currencyLoadedBefore == true)
                                R.string.updating_currencies_rates
                            else R.string.getting_currencies_rates
                    duration = Snackbar.LENGTH_SHORT
                }
            }
            Statuses.CONNECTING ->
                showSnack {
                    view = convert_parent
                    resId = R.string.connecting
                    duration = Snackbar.LENGTH_LONG
                    animationMode = Snackbar.ANIMATION_MODE_SLIDE
                }
        }
    }

    override fun finishDownloading() {
        networkFragment.cancelDownload()
    }

    override fun passException(url: String?, exception: Exception) {
        //means error occurred
        retry = true // post retry
        if (!isCurrency) return
        when (exception) {
            is SocketTimeoutException ->
                showSnack {
                    view = convert_parent
                    resId = R.string.time_out
                    duration = 12_500 //12.5 seconds
                    setAction {
                        resId = R.string.retry
                        listener = View.OnClickListener {
                            networkFragment.startDownload()
                        }
                    }
                }
            else ->
                showSnack {
                    view = convert_parent
                    resId = R.string.unable_to_get
                    duration = 12_500
                    setAction {
                        resId = R.string.retry
                        listener = View.OnClickListener {
                            networkFragment.startDownload()
                        }
                    }
                }
        }
    }

    private val preferencesSelected by lazy(LazyThreadSafetyMode.NONE) {
        LinkedHashMap<Int, Int>(4)
    }

    override fun getChosenValues(group: Int, child: Int) {
        preferencesSelected[group] = child
    }

    private var sliderValue = 5
    override fun getSliderValue(sliderValue: Int) {
        this.sliderValue = sliderValue
    }

    private var decimalFormatFactory: DecimalFormatFactory? = null
    override fun applyChanges() {
        decimalFormatFactory = DecimalFormatFactory().apply {
            buildDecimalFormatSymbols(this@ConvertActivity, preferencesSelected.values)
                    .apply {
                        Utils.decimalFormatSymbols = decimalFormatSymbols
                        Utils.pattern = pattern
                        Utils.numberOfDecimalPlace = sliderValue
                        if (!Utils.isEngineering)
                            Utils.pattern = setDecimalPlaces(pattern!!, sliderValue)
                        resetEditTexts()
                    }
        }
    }

    private fun resetEditTexts() {
        val editText =
                when {
                    firstEditText.isFocused -> firstEditText
                    secondEditText.isFocused -> secondEditText
                    else -> return
                }
        editText.apply {
            val text = SpannableStringBuilder(text)
            if (text.isNotEmpty()) {
                val cursorPosition = selectionEnd
                this.text = null
                this.text = text
                setSelection(cursorPosition)
            }
        }
    }
}