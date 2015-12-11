package views

import views.html.bootstrapField
import views.html.helper.FieldConstructor

object BootstrapHelper {
  implicit val bootstrapConstructor = FieldConstructor(bootstrapField.f)

}