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

  //Checl of label
  val taskForm = Form(tuple(
      "id"   -> number.verifying(min(0), max(9999)),
      "pass" -> nonEmptyText
      )
  )
  var score = 0
  def index = Action {
    Log.test_create()
    Redirect(routes.Application.login)
  }
  
  def login = Action {
    Ok(views.html.index(taskForm,"",false,score))
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
  
  def logOut() = Action {request =>
    Redirect(routes.Application.login).withNewSession
  }

}
