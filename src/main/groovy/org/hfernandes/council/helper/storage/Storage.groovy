package org.hfernandes.council.helper.storage

import groovy.sql.Sql
import org.h2.jdbcx.JdbcDataSource
import javax.sql.DataSource

class Storage {

    DataSource dataSource

    void init(){
        dataSource = new JdbcDataSource()
//        dataSource.setURL("jdbc:h2:/tmp/storage")
        dataSource.setURL("jdbc:h2:mem:storage")
    }


    void createTables(){
        Sql sql = new Sql(dataSource.connection)
        sql.execute("create table items (id int, name varchar, critical int, mastery int, haste int, versatility int)")
    }

    void loadItems(List items){
        Sql sql = new Sql(dataSource.connection)
        sql.withTransaction {
            sql.withBatch('insert into items(id, name, critical, mastery, haste, versatility) values (?, ?, ?, ?, ?, ?)') { ps ->
                items.each { item ->
                    ps.addBatch(item.id, item.name, item.critical, item.mastery, item.haste, item.versatility)
                }
            }
        }
    }

    List listItems(String id, String name){
        Sql sql = new Sql(dataSource.connection)

        if(id){
            return sql.rows("select * from items where id = $id")
        }
        if(name){
            return sql.rows("select * from items where name like  '%' || $name || '%'")
        }
        sql.rows("select * from items")
    }
}
