package com.example.unitconverter.constants

import com.example.unitconverter.miscellaneous.isNull
import kotlinx.io.IOException
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonInput
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.content
import org.jsoup.Jsoup
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader

val glm = LinkedHashSet<String>(200)
val fileArray = arrayOf(
    File("""C:\Users\otuok\OneDrive\Desktop\currency.json"""),
    File("""C:\Users\otuok\OneDrive\Desktop\values.json""")
)

///val token = "9b0294b1768717f520ec95bb81e8af3e83c3e45f"

@OptIn(ImplicitReflectionSerializer::class)
fun main() {

    val array = getListOfCurrencies()

    val apk = "92e635776474326b9648"//92e635776474326b9648  e8d24d245b82ee990d6d

    println(array.size)
    val setArray = LinkedHashSet(array).toMutableList()
    println(setArray.size)
    //println("${setArray == currenciesMap().keys.map { it }} help  $setArray")
    var ri = 1
    val linkedHashMap = LinkedHashMap<String, String>(setArray.size)
    println((0..setArray.size).step(2).map { it })
    while (true) {
        println("starting $apk $setArray ")

        for (i in (0..setArray.size).step(2)) {
            println("${ri++}")
            try {
                val url = "https://free.currconv.com/api/v7/" +
                        "convert?apiKey=$apk&q=${setArray[i]}_USD,${setArray[i + 1]}_USD"
                val j = Jsoup.connect(url).ignoreContentType(true).get()
                val l = Json.parse(kofi, j.body().text())
                linkedHashMap.putAll(l)
                l.forEach {
                    glm.add(it.key.substringBefore('_'))
                }
                println("$url  ${setArray[i]}  ${setArray[i + 1]} $l")
            } catch (e: IndexOutOfBoundsException) {
                println("last one")
                try {
                    val url = "https://free.currconv.com/api/v7/convert?" +
                            "apiKey=$apk&q=${setArray[i]}_USD&compact=y"
                    val j = Jsoup.connect(url).ignoreContentType(true).get()
                    val l = Json.parse(kof, j.body().text())

                    linkedHashMap.putAll(l)
                    l.forEach {
                        glm.add(it.key.substringBefore('_'))
                    }
                } catch (e: Exception) {
                    println("inner $e")
                }
            } catch (e: ClassCastException) {
                println(e)
            }
        }
        //save to string builder
        val stringBuilder = Json.stringify(linkedHashMap).run {
            StringBuilder(this.length).apply {
                this@run.forEach {
                    append(it)
                    if (it == ',')
                        append("\n\t")
                    if (it == ':')
                        append("  ")
                    if (it == '{')
                        append("\n\t")
                }
                delete(length - 1, length) //delete {
                append("\n}")
            }
        }
        //write to file
        val file = fileArray[1]
        file.writeText(stringBuilder.toString())
        println(glm)
        println(currenciesMap().size)
        val destination = """C:\Users\otuok\OneDrive\Desktop\unit_converter"""
        copyFiles(*fileArray, destination = destination)
        commitChanges()
        println("restarting ...")
        Thread.sleep(3610 * 1000 * 3) //update every 3 hours
    }

    /*if (shouldEdit) {
            val previousText = StringBuilder(file.readText())
            previousText.apply {
                delete(length - 2, length) //remove the last bracket and newline char
                append(',') //add the comma
            }
            stringBuilder.apply {
                delete(0, 2) // delete { and \n
            }

            previousText.append(stringBuilder)
            file.writeText(previousText.toString())
        } else {*/

    /*for (i in (0..arra.size - 1).step(2)) {
        try {
            val url = "https://free.currconv.com/api/v7/" +
                    "convert?apiKey=$apk&q=${arra[i]}_USD,${arra[i + 1]}_USD"
            val j = Jsoup.connect(url).ignoreContentType(true).get()
            val l = Json.parse(kofi, j.body().text())
            linkedHashMap.putAll(l)
            println("$url  ${arra[i]}  ${arra[i + 1]} $l")
        } catch (e: Exception) {
            println(e)
            val url =
                "https://free.currconv.com/api/v7/convert?apiKey=$apk&q=${arra[i]}_USD&compact=y"
            try {
                val j = Jsoup.connect(url).ignoreContentType(true).get()
                val l = Json.parse(kof, j.body().text())
                linkedHashMap.putAll(l)
                we = url
                println(url)
                println(l)
            } catch (e: Exception) {
                println(e)
            }
        }
    }*/
/*
    val ne =
        """{"query":{"count":2},"results":{"XPF_USD":{"id":"XPF_USD","fr":"XPF","to":"USD","val":0.00908},"YER_USD":{"id":"YER_USD","fr":"YER","to":"USD","val":0.003996}}}"""
    Json.parse(kofi, ne)

    fun removeLastBracket() {
        File("""C:\Users\otuok\OneDrive\Desktop\my.json""")
    }*/
}

fun commitChanges() {
    ProcessBuilder()
        .command("cmd.exe", "/c", """C:\Users\otuok\OneDrive\Desktop\shell.sh""")
        .start()
        .apply {
            val error = Results(errorStream, "ERROR")
            val other = Results(inputStream, "OTHER")
            error.start()
            other.start()
            waitFor()
            println("error ${error.getOutput()}")
            println("info ${other.getOutput()}")
        }
}

fun copyFiles(vararg files: File, destination: String) {
    val dir = File(destination)
    assert(dir.isDirectory)
    files.forEach {
        //creating file
        File(destination, it.name).apply {
            createNewFile()
            writeText(it.readText())
        }
    }
}

fun getListOfCurrencies(): MutableList<String> {
    val nHtml = Jsoup.connect("https://en.wikipedia.org/wiki/List_of_circulating_currencies").get()
    println(nHtml.data().length)
    val arra = ArrayList<String>(300)
    nHtml.getElementsByTag("tr").forEachIndexed { index, element ->
        try {
            println(
                "$index ${element.select("td")[element.select("td").lastIndex - 2].text()
                    .substringBefore('[')}"
            )
            val k =
                element.select("td")[element.select("td").lastIndex - 2].text().substringBefore('[')
            if ("none" !in k)
                arra.add(k)
        } catch (e: Exception) {
            println(e)
        }
    }
    return arra.toSet().toMutableList()
}

fun currenciesMap(): LinkedHashMap<String, String> {
    val nHtml = Jsoup.connect("https://en.wikipedia.org/wiki/List_of_circulating_currencies").get()

    println(nHtml.data().length)

    val map = LinkedHashMap<String, String>(200)
    val e = LinkedHashSet<String>(200)
    var rowNumber: Int? = null
    for (element in nHtml.getElementsByTag("tr")) {
        try {
            try {
                rowNumber = element.select("td[rowspan]").attr("rowspan").toInt()
            } catch (e: Exception) {
                //println(element.select("td[rowspan]").attr("rowspan"))
                //println(e)
            }
            var cur_name: String

            val cur_symbol =
                element.select("td")[element.select("td").lastIndex - 2].text().substringBefore('[')
                    .run {
                        if ("none" in this)
                            ""
                        else this
                    }
            val curr_sym = element.select("td")[element.select("td").lastIndex - 3]
                .text()
                .substringBefore(' ')
                .substringBefore(',')
            println(
                "${element.select("td[data-sort-value]").eachText().filter { "none" !in it }
                    .map { it.substringBefore('[') }} kofi  ${element.select("a[title]")
                    .eachText()}"
            )
            if (rowNumber != null) {
                cur_name = element.select("a[title]").eachText()[0]
                rowNumber -= 1
                if (rowNumber == 0) rowNumber = null

            } else {
                cur_name = element.select("a[title]").eachText()[1]
            }
            if (cur_symbol.isEmpty())
                continue
            else if ("none" in curr_sym)
                continue
            else {
                if (cur_symbol !in e && cur_symbol in glm)
                    map.put("$cur_name ($cur_symbol)", curr_sym)
                e.add(cur_symbol)
            }
        } catch (e: Exception) {
            println(e)
        }
    }
    println("${map.size}  ${e.size}")
    /*map.forEach {
        println(it)
    }*/
    @OptIn(ImplicitReflectionSerializer::class)

    Json.stringify(map).run {
        StringBuilder(this.length).apply {
            this@run.forEach {
                append(it)
                if (it == ',') {
                    append("\n\t")
                }
                if (it == ':')
                    append("  ")
                if (it == '{')
                    append("\n\t")
            }
            delete(length - 1, length) //delete {
            append("\n}")
        }.toString().apply {
            fileArray[0].writeText(this)
        }
    }
    return map
}


class Results(private val inputStream: InputStream, private val type: String) : Thread() {

    private val output = StringBuffer();

    override fun run() {
        try {
            val isr = InputStreamReader(inputStream)
            val br = BufferedReader(isr)
            while (true) {
                val line = br.readLine()
                if (line.isNull())
                    break
                println("$type> $line")
                output.append(line + "\r\n")
            }
        } catch (e: IOException) {
            e.printStackTrace();
        }
    }

    fun getOutput() = output.toString()
}

//deserializers
object kofi : DeserializationStrategy<Map<String, String>> {
    override val descriptor: SerialDescriptor
        get() = TODO("Not yet implemented")

    override fun deserialize(decoder: Decoder): Map<String, String> {
        val input = decoder as JsonInput
        val array = input.decodeJson() as JsonObject
        return LinkedHashMap<String, String>(array.size).apply {

            array.forEach {
                val k = it.value as JsonObject
                k.forEach {
                    println(it)
                    try {
                        println("${(it.value as JsonObject)["val"]?.content}")
                        (it.value as JsonObject)["val"]?.content?.let { it1 ->
                            put(
                                it.key,
                                it1
                            )
                        }
                    } catch (e: Exception) {
                        println(e)
                    }
                }
                //println("$it\nk  $k")
            }
        }
    }

    override fun patch(decoder: Decoder, old: Map<String, String>): Map<String, String> {
        TODO("Not yet implemented")
    }
}

object kof : DeserializationStrategy<Map<String, String>> {
    override val descriptor: SerialDescriptor
        get() = TODO("Not yet implemented")

    override fun deserialize(decoder: Decoder): Map<String, String> {
        val input = decoder as JsonInput
        val array = input.decodeJson() as JsonObject
        return LinkedHashMap<String, String>(array.size).apply {

            array.forEach {
                try {
                    val value = (it.value as JsonObject)["val"]?.content
                    if (value != null) {
                        put(it.key, value)
                    }
                    println((it.value as JsonObject)["val"]?.content)
                } catch (e: Exception) {
                    println(e)
                }
            }
        }
    }

    override fun patch(decoder: Decoder, old: Map<String, String>): Map<String, String> {
        TODO("Not yet implemented")
    }
}

/*
val p = """{"ZMW_USD":{"val":0.053963}}"""
Json.parse(kof,p)
object kof: DeserializationStrategy<Map<String, String>> {
    override val descriptor: SerialDescriptor
        get() = TODO("Not yet implemented")

    override fun deserialize(decoder: Decoder): Map<String, String> {
        val input = decoder as JsonInput
        val array = input.decodeJson() as JsonObject
        return LinkedHashMap <String,String>(array.size).apply {

            array.forEach {
                val value = (it.value as JsonObject)["val"]?.content
                if (value != null) {
                    put(it.key,value)
                }
                println((it.value as JsonObject)["val"]?.content)
            }
        }
    }

    override fun patch(decoder: Decoder, old: Map<String, String>): Map<String, String> {
        TODO("Not yet implemented")
    }
}*/