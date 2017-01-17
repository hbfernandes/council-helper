package org.hfernandes.council.helper

import org.hfernandes.council.helper.scrap.WowHead
import org.hfernandes.council.helper.storage.Storage

import static spark.Spark.*

class Main {

    static void main(String[] args){
        Storage storage = new Storage()
        storage.init()

        // while on dev create everything when starting
        storage.createTables()

        List items = new WowHead().getItems()
        storage.loadItems(items)

        port(9090)

        // Endpoints
        post('/load', { req, res ->
            // add parameters for this search eventually
//            List items = new WowHead().getItems()
//
//            storage.loadItems(items)
        })

        get("/list", { req, res ->
            res.type("application/json")
            try{
                storage.listItems(
                        req.queryMap().get('id').value(),
                        req.queryMap().get('name').value())

            }catch(e){
               println(e)
            }
        }, new JsonTransformer())
    }

}