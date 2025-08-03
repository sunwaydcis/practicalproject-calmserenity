package ch.makery.address.view
import ch.makery.address.model.Person
import ch.makery.address.MainApp
import ch.makery.address.util.DateUtil.asString
import javafx.fxml.FXML
import javafx.scene.control.{Label, TableColumn, TableView, TextField}
import scalafx.Includes.*
import scalafx.beans.binding.Bindings
import scalafx.scene.control.Alert
import javafx.event.ActionEvent
import scala.util.{Success, Failure}

@FXML
class PersonOverviewController():
  @FXML
  private var personTable: TableView[Person] = null
  @FXML
  private var firstNameColumn: TableColumn[Person, String] = null
  @FXML
  private var lastNameColumn: TableColumn[Person, String] = null
  @FXML
  private var firstNameLabel: Label = null
  @FXML
  private var lastNameLabel: Label = null
  @FXML
  private var streetLabel: Label = null
  @FXML
  private var postalCodeLabel: Label = null
  @FXML
  private var cityLabel: Label = null
  @FXML
  private var birthdayLabel: Label = null


  // initialize Table View display contents model
  def initialize(): Unit =
    // initialize Table View display contents model
    personTable.items = MainApp.personData
    // initialize columns's cell values
    firstNameColumn.cellValueFactory = {_.value.firstName}
    lastNameColumn.cellValueFactory = {_.value.lastName}

    showPersonDetails(None)

    personTable.selectionModel().selectedItem.onChange(
      (_, _, newValue) => showPersonDetails(Option(newValue))
    )


  private def showPersonDetails(person: Option[Person]): Unit =
    person match
      case Some(person)=>
      //Fill the labels with info from the person object
        firstNameLabel.text <==person.firstName
        lastNameLabel.text <== person.lastName
        streetLabel.text <== person.street
        cityLabel.text <== person.city;
        postalCodeLabel.text <== person.postalCode.delegate.asString()
        birthdayLabel.text   <== Bindings.createStringBinding(()=>{
          person.date.value.asString
      }, person.date)

      case None =>
        // Person is null, remove all the text.
        firstNameLabel.text.unbind()
        lastNameLabel.text.unbind()
        streetLabel.text.unbind()
        postalCodeLabel.text.unbind()
        cityLabel.text.unbind()
        birthdayLabel.text.unbind()


        firstNameLabel.text = ""
        lastNameLabel.text = ""
        streetLabel.text = ""
        postalCodeLabel.text = ""
        cityLabel.text = ""
        birthdayLabel.text = ""


  def handleDeletePerson(action : ActionEvent) =
    val selectedIndex = personTable.selectionModel().selectedIndex.value
    val selectedPerson = personTable.selectionModel().selectedItem.value
    if (selectedIndex >= 0) then
      selectedPerson.save() match
        case Success(x) =>
          personTable.items().remove(selectedIndex);
        case Failure(e) =>
          val alert = new Alert(Alert.AlertType.Warning) {
            initOwner(MainApp.stage)
            title = "Failed to Save"
            headerText = "Database Error"
            contentText = "Database problem filed to save changes"
          }
            alert.showAndWait()
    else
      // Nothing selected.
      val alert = new Alert(Alert.AlertType.Warning) {
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Person Selected"
        contentText = "Please select a person in the table."
      }
        .showAndWait()


  def handleNewPerson(action: ActionEvent) =
    val person = new Person("", "")
    val okClicked = MainApp.showPersonEditDialog(person)
    if (okClicked) then
      person.save() match
        case Success(x) =>
          MainApp.personData += person
        case Failure(e) =>
          val alert = new Alert(Alert.AlertType.Warning) {
            initOwner(MainApp.stage)
            title = "Failed to Save"
            headerText = "Database Error"
            contentText = "Database problem filed to save changes"
          }
            alert.showAndWait()


  def handleEditPerson(action: ActionEvent) =
    val selectedPerson = personTable.selectionModel().selectedItem.value
    if (selectedPerson != null) then
      val okClicked = MainApp.showPersonEditDialog(selectedPerson)

      if (okClicked) then
        selectedPerson.save() match
          case Success(x) =>
            showPersonDetails(Some(selectedPerson))
          case Failure(e) =>
            val alert = new Alert(Alert.AlertType.Warning) {
              initOwner(MainApp.stage)
              title = "Failed to Save"
              headerText = "Database Error"
              contentText = "Database problem filed to save changes"
            }
              alert.showAndWait()
    else
      // Nothing selected.
      val alert = new Alert(Alert.AlertType.Warning) {
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Person Selected"
        contentText = "Please select a person in the table."
      }
        alert.showAndWait()







