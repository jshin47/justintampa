import scalatags.Text.all._

object ClientStaticAssets {

  def index = {
    "<!DOCTYPE html>" + html(
      head(
        base(href := "/"),
        meta(name := "viewport", content := "width=device-width, initial-scale=1.0"),
        link(rel := "shortcut icon", href := "favicon.ico", `type` := "image/x-icon")
      ),
      body(
        // Empty
      )
    )
  }

  def style = {
    """
      |td.buttons {
      |    text-align: center;
      |}
      |
      |.panel-primary .panel-head-buttons .glyphicon {
      |    color: white;
      |}
      |
      |.glyphicon {
      |    margin-left: 2px;
      |    margin-right: 2px;
      |}
      |
      |.panel-title .glyphicon {
      |    margin-right: 10px;
      |}
    """.stripMargin
  }

  def highlightJsStyle = "monokai-sublime"


}