package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Log(id: Int, pass:String)

object Log {
  val log = {
    get[Int]("id") ~
    get[String]("pass") map{
      case id~pass => Log(id, pass)
    }
  }
  
  def test_create(){
    DB.withConnection{implicit c =>
      SQL("insert into user_info (id,pass) values({id},{pass})").on(
          'id -> 3,
          'pass -> "pass3"
      ).executeInsert()
    }
  }
  
  def getId(i:Int): Boolean = DB.withConnection { implicit c =>
    val result:List[(Int,String)] = SQL("select * from user_info")
    .as(
    int("id") ~ str("pass") map(flatten) *
    )
    val b = (result(0))._1
    if (b == i){
      return true  
    }else{
      return false
    }
  }
  
  def getPass(s:String): Boolean = DB.withConnection { implicit c =>
    val result:List[(Int,String)] = SQL("select * from user_info")
    .as(
    int("id") ~ str("pass") map(flatten) *
    )
    val b = (result(0))._2
    if (b == s){
      return true  
    }else{
      return false
    }
  }
  
  def all(): List[Log] = DB.withConnection { implicit c =>
    SQL("select * from user_info").as(log *)
  }
}