package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Log(
    id: Int, 
    name: String,
    mail: String,
    pass:String,
    point: Int
    )

object Log {
  val log = {
    get[Int]("id") ~
    get[String]("name") ~
    get[String]("mail") ~
    get[String]("pass") ~
    get[Int]("point") map{
      case id~name~mail~pass~point => Log(id,name,mail,pass,point)
    }
  }
  
  def test_create(){
    DB.withConnection{implicit c =>
      SQL("insert into user_info (id,name,mail,pass,point) values({id},{name},{mail},{pass},{point})").on(
          'id -> 4,
          'name -> "takeda",
          'mail -> "4@admin.tes",
          'pass -> "pass4",
          'point -> 40
      ).executeInsert()
    }
    DB.withConnection{implicit c =>
      SQL("insert into user_info (id,name,mail,pass,point) values({id},{name},{mail},{pass},{point})").on(
          'id -> 3,
          'name -> "take",
          'mail -> "3@admin.tes",
          'pass -> "pass3",
          'point -> 30
      ).executeInsert()
    }
  }
  
  def getId(i:Int,count:Int): Boolean = DB.withConnection { implicit c =>
    val result:List[(Int,String)] = SQL("select * from user_info")
    .as(
    int("id") ~ str("pass") map(flatten) *
    )
    if (count >= result.length){
      return false
    }else{
      var bi = (result(count))._1
      if (bi == i){
        return true  
      }else{
        return getId(i,(count+1))
      }
    }
  }
  
  def getPass(s:String, count:Int): Boolean = DB.withConnection { implicit c =>
    val result:List[(Int,String)] = SQL("select * from user_info")
    .as(
    int("id") ~ str("pass") map(flatten) *
    )
    if (count >= result.length){
      return false 
    }else{
      var bs = (result(count))._2
      if (bs == s){
    	  return true
      }else{
    	  return getPass(s,(count+1))
      }
    }
  }
  
  def returnScore(id:Int): Int = DB.withConnection { implicit c =>
    val result:List[(String,Int)] = SQL("select * from user_info where id="+id)
    .as(
    str("pass") ~ int("point") map(flatten) *
    )
    return result(0)._2
  }
  
  def all(): List[Log] = DB.withConnection { implicit c =>
    SQL("select * from user_info").as(log *)
  }
}