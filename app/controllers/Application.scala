package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Form._
import play.api.data.Forms._
import models.Log
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
//for DB
import anorm._
import anorm.SqlParser._

import play.api.db._
import play.api.Play.current
import play.api.data.validation.Constraints._


class Application extends Controller {

  //Check of id and pass
  //もし拡張する際はtupleではなくmappingとしたい
  val taskForm = Form(tuple(
      "id"   -> number.verifying(min(0), max(9999)),
      "pass" -> nonEmptyText
      )
  )
  val taskForm1 = Form(tuple(
      "id"   -> number.verifying(min(0), max(9999)),
      "name" -> nonEmptyText,
      "mail" -> nonEmptyText,
      "pass" -> nonEmptyText
      )
  )
  var score = 0
  def index = Action {
    Log.test_create()
    Redirect(routes.Application.login).withNewSession
  }
  
  def login = Action.apply{ request =>
    request.session.get("connect").map{ user =>
      Redirect(routes.Application.page)   
    }getOrElse{
      Ok(views.html.index(taskForm,"",false,score)).withNewSession
    }
    
  }
  
  def page = Action.apply { request =>  
    request.session.get("connect").map{ user =>
      Ok(views.html.index(taskForm,"Welcome!",true, score)) 
    }getOrElse{
      Ok(views.html.index(taskForm,"Please login again.",false,score))
    }
  }
  
  def logAuth() = Action { implicit req =>
    taskForm.bindFromRequest.fold(
        errors => Redirect(routes.Application.page),
        value => {
          val haveId = value._1
          if (Log.getId(haveId,0)==true && Log.getPass(value._2,0)==true){
           score = Log.returnScore(value._1)
           Redirect(routes.Application.page).withSession("connect" -> (value._1).toString())
          }else{
           Redirect(routes.Application.page)
          }
        }
    )
  }
  
  def logAdmin() = Action{ req =>
    Ok(views.html.admin(taskForm1,"Regist now.",false))
  }
  
  def registAd() = Action{ implicit req =>
    taskForm1.bindFromRequest.fold(
        errors => Ok(views.html.admin(taskForm1,"Please regist again.",false)),
        value1 => {
          //if(Log.confirm(value)){
          //登録内容確認画面追加予定
          Log.confirm(value1)
          score = Log.returnScore(value1._1)
          Redirect(routes.Application.page).withSession("connect" -> (value1._1).toString)
          /*}else{
            Ok(views.html.admin(taskForm1,"Please regist again.",false))
          }*/
        }
    )
  }
  
  def logOut() = Action {request =>
    score = 0
    Redirect(routes.Application.login).withNewSession
  }

}
