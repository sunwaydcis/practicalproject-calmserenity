package ch.makery.address

import ch.makery.address.model.Person
import ch.makery.address.util.Database
import ch.makery.address.view.PersonEditDialogController
import javafx.fxml.FXMLLoader
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes.*
import javafx.scene as jfxs
import scalafx.collections.ObservableBuffer
import scalafx.stage.{Modality, Stage}

object MainApp extends JFXApp3:

  Database.setupDB()

  ///Window Root Pane
  var roots: Option[scalafx.scene.layout.BorderPane] = None
  //stylesheet
  var cssResource = getClass.getResource("view/DarkTheme.css")

  val personData = new ObservableBuffer[Person]()

  override def start(): Unit =

    personData ++= Person.getAllPersons
    
    val rootResource = getClass.getResource("view/RootLayout.fxml")
    val loader= new FXMLLoader(rootResource)
    loader.load()

    roots = Option(loader.getRoot[jfxs.layout.BorderPane])

    stage = new PrimaryStage():
      title = "AddressApp"
      scene = new Scene():
        stylesheets = Seq(cssResource.toExternalForm)
        root = roots.get
        showPersonOverview()

    def showPersonOverview(): Unit =
      val resource = getClass.getResource("view/PersonOverview.fxml")
      val loader = new FXMLLoader(resource)
      loader.load()
      val roots = loader.getRoot[jfxs.layout.AnchorPane]
      this.roots.get.center = roots

  def showPersonEditDialog(person: Person): Boolean =
    val resource = getClass.getResource("view/PersonEditDialog.fxml")
    val loader = new FXMLLoader(resource)
    loader.load();
    val roots2 = loader.getRoot[jfxs.Parent]
    val control = loader.getController[PersonEditDialogController]

    val dialog = new Stage():
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      scene = new Scene():
        stylesheets = Seq(cssResource.toExternalForm)
        root = roots.get


    control.dialogStage = dialog
    control.person = person
    dialog.showAndWait()
    control.okClicked









