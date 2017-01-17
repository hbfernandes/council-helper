package org.hfernandes.council.helper

import groovy.json.JsonBuilder
import spark.ResponseTransformer

class JsonTransformer implements ResponseTransformer {

    String render(Object model) {
        new JsonBuilder( model ).toPrettyString()
    }

}