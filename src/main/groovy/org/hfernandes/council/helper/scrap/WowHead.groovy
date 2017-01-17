package org.hfernandes.council.helper.scrap

import com.google.json.JsonSanitizer
import groovy.json.JsonSlurper

class WowHead {

    String filter = '213:146;8025:1;0:0' // Nighthold heroic items
    String url = 'http://www.wowhead.com/items?filter='

    List getItems(){
        String html = new URL("$url$filter").getText()

        // there is a variable in a JS block in the middle of the html with the list of results
        // (...)
        // var listviewitems = [<list>];
        // (...)
        // lets grab it! :)

        List items = []

        def matcher = html =~ /(?s).*var listviewitems = (?<itemList>.*?);.*/
        if(matcher.matches()){
            // not valid json, clean it up
            String validJson = JsonSanitizer.sanitize(matcher.group('itemList'))

            JsonSlurper slurper = new JsonSlurper()
            items = slurper.parseText(validJson)
        }

        items.collect { item ->
            int total = [item.critstrkrtng?:0, item.versatility?:0, item.hastertng?:0, item.mastrtng?:0].sum()

            [
                id: item.id,
                name: item.name[1..-1],
                critical: item.critstrkrtng ? Math.round(item.critstrkrtng/total * 100) : 0,
                versatility: item.versatility ? Math.round(item.versatility/total * 100) : 0,
                haste: item.hastertng ? Math.round(item.hastertng/total * 100) : 0,
                mastery: item.mastrtng ? Math.round(item.mastrtng/total * 100) : 0
            ]
        }
    }


}
