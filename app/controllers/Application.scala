package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Form._
import play.api.data.Forms._
import models.Log
//以下の二文は必須(原因不明)html文の方にも追加記載
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
//データベース操作(build.sbtに記述。2.4において記述が文献と異なるので注意)
import anorm._
import anorm.SqlParser._

import play.api.db._
import play.api.Play.current
import play.api.data.validation.Constraints._

class Application extends Controller {

  //ユーザによって入力されたlabelが空ではないことをチェック
  val taskForm = Form(mapping(
      "id"   -> number.verifying(min(0), max(9999)),
      "pass" -> nonEmptyText  
      )(Log.apply)(Log.unapply)
  )
  var boolog = false
  def index = Action {
    Log.test_create()
    Redirect(routes.Application.login)
  }
  
  def login = Action {
    Ok(views.html.index(taskForm,"",boolog))
  }
  
  def page = Action.apply { request =>  
    request.session.get("connect").map{ user =>
      Ok(views.html.index(taskForm,"Welcome!",true)) 
    }getOrElse{
      Ok(views.html.index(taskForm,"Please login again.",false))
    }
  }
    
  def logAuth() = Action { implicit req =>
    taskForm.bindFromRequest.fold(
        errors => Redirect(routes.Application.page),
        value => {
          if (Log.getId(value.id)==true && Log.getPass(value.pass)==true){
           Redirect(routes.Application.page).withSession("connect" -> value.pass)
          }else{
           Redirect(routes.Application.page)
          }
        }
    )
  }
  
  def logOut() = Action {request =>
    boolog=false
    Redirect(routes.Application.login).withNewSession
  }

}
