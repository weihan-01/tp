---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# Neighbourly Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org).

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [
`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [
`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in
charge of the app launch and shut down.

* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues
the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API
  `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using
the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component
through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the
implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in  
[`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts such as `CommandBox`, `ResultDisplay`, `SeniorListPanel`, `CaregiverListPanel`, `StatusBarFooter`, etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts is defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the  
[`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java)  
is specified in  
[`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component:

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Senior` and `Caregiver` objects residing in the `Model`.


### Logic component

**API** : [
`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete s/1")` API
call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete s/1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of
PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates
   a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which
   is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take
   several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:

* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a
  placeholder for the specific command name e.g., `AddSeniorCommandParser`) which uses the other classes shown above to parse
  the user command and create a `XYZCommand` object (e.g., `AddSeniorCommand` or `AddCaregiverCommand`) which the `AddressBookParser` returns back as a
  `Command` object.
* All `XYZCommandParser` classes (e.g., `AddSeniorCommandParser`, `AddCaregiverCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser`
  interface so that they can be treated similarly where possible e.g, during testing.

### Model component

**API** : [
`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which
  is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to
  this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a
  `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they
  should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. 

<puml src="diagrams/BetterModelClassDiagram.puml" width="800" />

</box>

### Storage component

**API** : [
`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,

* can save both address book data and user preference data in JSON format, and read them back into corresponding
  objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only
  the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects
  that belong to the `Model`)

Additionally, `JsonSerializableAddressBook` now serializes two separate collections:
* `List<JsonAdaptedSenior`
* `List<JsonAdaptedCaregiver`
Each JSON adapter is responsible for validating fields and converting between the JSON representation
and the model types (`Senior` / `Caregiver`). Only seniors maintain a risk tag, stored as a single-element 
list of JsonAdaptedTag. Caregivers do not contain any tags.

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo
history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the
following operations:

* `VersionedAddressBook#commit()`— Saves the current address book state in its history.
* `VersionedAddressBook#undo()`— Restores the previous address book state from its history.
* `VersionedAddressBook#redo()`— Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and
`Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the
initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls
`Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be
saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls
`Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will
not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the
`undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once
to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no
previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the
case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the
lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once
to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address
book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()`
to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as
`list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus,
the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not
pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be
purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern
desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
    * Pros: Easy to implement.
    * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
    * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
    * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* has a need to manage a significant number of elderly and caregiver contacts
* wants to know if at-risk seniors are not assigned caregivers
* needs to record senior health conditions
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: Despite the slate of funding put into ageing initiatives such as the Age Well SG programme,
including the network expansion of Active Ageing Centres (AACs) in Singapore for eight in ten seniors to have access to
AAC activities in the vicinity of their homes, many elderly continue to remain at the boundaries of Singapore's social
care and aged care system. This phenomenon leads to reduced overall life satisfaction with feelings of social isolation.
As such, there is a need for community efforts to actively seek out and bring social networks closer to engage. Having
scattered contacts around the housing estates is a challenge for NGOs and social workers to navigate to coordinate care
for the elderly with other stakeholders such as caretakers, nurses, elderly, children, volunteers etc. As such, it is
important to for eldercare organisations to maintain a centralised, easy-to-use contact book to keep track of the
specialised support needed by elderly, strengthen collaboration and provide coordinated support to elderly in the
community.

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​            | I want to …​                                         | So that I can…​                                             |
|----------|--------------------|------------------------------------------------------|-------------------------------------------------------------|
| `* * *`  | new user           | view the user guide easily                           | learn more about the product as and when I need             |
| `* * *`  | busy user          | search for a senior by name                          | instantly retrieve their details during field visits        |
| `* * *`  | time-pressed user  | flag urgent seniors                                  | immediately identify high-priority cases during my work     |
| `* * *`  | messy user         | tag seniors with flexible labels                     | find them later even if my notes are scattered              |
| `* * *`  | long-time user     | see elderly who are not assigned any caregiver       | immediately identify high-priority cases during my work     |
| `* * *`  | messy user         | tag caregivers with flexible labels                  | find them later even if my notes are scattered              |
| `* * *`  | new user           | add a new senior with minimal fields                 |                                                             |
| `* * *`  | user               | delete a senior                                      | remove entries that I no longer need                        |
| `* *`    | new user           | see sample data when I first open the app            | quickly understand how the system looks when populated      |
| `* *`    | long-time user     | archive seniors who no longer need support           | keep my records tidy without losing history                 |
| `* *`    | sharing user       | generate a PDF report of all or specific seniors     | print / share information with others                       |
| `* *`    | messy user         | view recently modified seniors                       | quickly return to what I was last working on                |
| `* *`    | long-time user     | update caregivers who may no longer be in the org    | flag out elderly who may not have a caregiver currently     |
| `* *`    | curious user       | see insights of number of elderly, caregivers, vols  | share these statistics for recruitment                      |
| `* *`    | meticulous user    | perform batch delete of seniors or caregivers by tag | keep my records tidy                                        |
| `* *`    | meticulous user    | add comprehensive senior particulars                 | remember more details about these elderly                   |
| `* *`    | returning user     | edit a senior’s details                              | keep records up to date                                     |
| `* *`    | outreach associate | mark a senior as “visited today”                     | log my field work                                           |
| `* *`    | long-time user     | pin important seniors                                | always find them at the top of the list                     |
| `* *`    | long-time user     | undo the last action                                 | quickly recover from mistakes                               |
| `* *`    | long-time user     | redo an undone action                                | restore my intended changes                                 |
| `* *`    | long-time user     | generate a summary report of seniors by tag          | have a quick overview of all the information                |
| `* *`    | long-time user     | mark a caregiver as “inactive”                       | know they are no longer available                           |
| `* *`    | long-time user     | customize tag colors                                 | visually distinguish categories to my preference            |
| `* *`    | long-time user     | be warned when adding a duplicate senior             | don’t accidentally create two records                       |
| `* *`    | long-time user     | import seniors from a CSV file                       | migrate data quickly                                        |
| `* *`    | meticulous user    | write some notes on each senior                      | key in certain details that the tags do not consider        |
| `* *`    | long-time user     | sort the seniors by various tags or attributes       | arrange my address book in my personal preferred state      |
| `* *`    | organised user     | search & filter seniors by medical, language, etc.   | plan outreach activities more effectively                   |
| `* *`    | organised user     | search & filter seniors by medical, language, etc.   | plan outreach activities more effectively                   |
| `*`      | poor eyesight user | enlarge font on GUI                                  | see the letters and words properly                          |
| `*`      | quirky user        | change font on GUI                                   | change the font to my liking                                |
| `*`      | quirky user        | hidden easter eggs                                   | find weird things put in the app by the dev                 |
| `*`      | blind user         | text to speech                                       | talk to command instead of typing                           |
| `*`      | new user           | light mode                                           | see more clearly the text                                   |
| `*`      | forgetful user     | set reminders for follow-ups or scheduled visits     | no senior is unintentionally overlooked in our care efforts |                    |                                                      |                                                         |
| `* `     | busy user          | view volunteer availability & match with seniors     | optimize resources and reduce scheduling conflicts          |                    |                                                      |                                                             |

*{More to be added}*

### Use cases

(For all use cases below, the **System** is the `AddressBook` and the **Actor** is the `user`, unless specified
otherwise)

**Use case 1: View User Guide Easily**

**MSS**

1. User opens the app
2. User types the command "help"
3. System displays a link to user guide
4. User browses or searches for relevant topic

   Use case ends.

**Extensions**

* 2a. Invalid command

    * 2a1. AddressBook shows an error message. - "Command format invalid"

      Use case resumes at step 2.

Command Format
help

**Use case 2: Add senior contact: add-snr**

**MSS**

1. User creates a new senior record in system with the command "add-snr"
2. User need to minimally key in name, risk tag, phone, and address for "add-snr" command
3. User can optionally key in notes and caregiver id for "add-snr" command
4. New senior record is added

   Use case ends.

**Extensions**

* 2a. Invalid risk tag

    * 2a1. AddressBook shows an error message. - "Invalid risk tag. Risk tag must either be
    * `High Risk` or `HR`, `Medium Risk` or `MR`, or `Low Risk` or `LR`."

      Use case resumes at step 2.

* 2b. Invalid phone

    * 2b1. AddressBook shows an error message. - “Phone number must be 8 digits.”

      Use case resumes at step 1.

* 2c. Missing name

    * 2c1. AddressBook shows an error message. - “Senior name cannot be empty.”

      Use case resumes at step 1.

* 2d. Missing risk tag

    * 2d1. AddressBook shows an error message. - "Senior must be assigned a risk tag."

      Use case resumes at step 1.

* 2e. Missing phone number

    * 2e1. AddressBook shows an error message. - "Senior phone number cannot be empty."

      Use case resumes at step 1.

* 2e. Missing address

    * 2e1. AddressBook shows an error message. - "Senior address cannot be empty."

      Use case resumes at step 1.

* 3a. Invalid caregiver ID

    * 3a1. AddressBook shows an error message. - "No such caregiver exists."

      Use case resumes at step 1.

* 4a. Duplicate detected

    * 4a1. AddressBook shows an error message. - “Senior already exists. Please amend your entry.”

      Use case resumes at step 1.

Command Format

add-snr n/NAME t/RISK_TAG p/PHONE a/ADDRESS [n/NOTES] [c/CAREGIVER_ID]

Example Commands

add-snr n/Lim Ah Kow t/High Risk p/91234567 a/Blk 123 Bedok North Rd #02-45 n/Has dementia c/201

**Use case 3: Add caregiver contact: add-cgr**

**MSS**

1. User creates a new caregiver record in system with the command "add-cgr"
2. User need to minimally key in name and phone for "add-cgr" command
3. User can optionally key in notes and address for "add-cgr" command
4. New caregiver record is added

   Use case ends.

* 2a. Invalid phone

    * 2a1. AddressBook shows an error message. - “Phone number must be 8 digits.”

      Use case resumes at step 1.

* 2b. Missing name

    * 2b1. AddressBook shows an error message. - “Caregiver name cannot be empty.”

      Use case resumes at step 1.

* 2c. Missing phone number

    * 2c1. AddressBook shows an error message. - "Caregiver phone number cannot be empty."

      Use case resumes at step 1.

* 4a. Duplicate detected

    * 4a1. AddressBook shows an error message. - “Caregiver already exists. Please amend your entry.”

      Use case resumes at step 1.

**Use case 4: Delete senior / caregiver contact: delete**

**MSS**

1. User requests to delete a specific person in the list
2. AddressBook deletes the person

   Use case ends.

**Extensions**

* 2a. Invalid index

    * 2a1. AddressBook shows an error message. - “No such index exists. Please ensure the index matches a person from
      the database.”

      Use case resumes at step 2.

* 2b. Missing index

    * 2a1. AddressBook shows an error message. - “Index cannot be empty.”

      Use case resumes at step 2.

Command Format

1. delete s/SENIOR_INDEX
2. delete c/CAREGIVER_INDEX

Example Commands

delete s/3

**Use case 5: Assigning caregiver to senior: assign**

**MSS**

1. User assigns caregiver to a senior using the command "assign"
2. AddressBook reflects senior's allocation to caregiver

   Use case ends.

**Extensions**

* 1a. Invalid senior index

    * 2a1. AddressBook shows an error message. - “No such senior index exists. Please ensure the index matches a senior
      from the database.”

      Use case resumes at step 1.

* 1b. Invalid caregiver index

    * 1b1. AddressBook shows an error message. - “No such caregiver index exists. Please ensure the index matches a
      caregiver from the database.”

      Use case resumes at step 2.

* 1c. Missing senior index

    * 1c1. AddressBook shows an error message. - “Senior index cannot be empty.”

      Use case resumes at step 2.

* 1d. Missing caregiver index

    * 1d1. AddressBook shows an error message. - “Caregiver index cannot be empty.”

      Use case resumes at step 2.

Command Format

assign s/SENIOR_INDEX c/CAREGIVER_INDEX

Example Commands

assign s/1 c/3

**Use case 6: Pin a contact (Caregiver or Senior): pin**

**MSS**

1. User pins a contact using the command "pin n/NAME"
2. AddressBook highlights the contact and move it to the top of the list. Any previously pinned contact is unpinned.

   Use case ends.

**Extensions**

* 1a. Missing name (pin n/)

    * 1a1. AddressBook shows an error message. - "Invalid command format! pin n/NAME"

      Use case resumes at step 1.

* 1b. Wrong prefix used (e.g. pin /Yap Mei Ting)

    * 1b1. AddressBook shows an error message. - "Invalid command format! pin n/NAME"

      Use case resumes at step 1.

* 1c. Name not found in AddressBook

    * 1c1. AddressBook shows an error message. - “No person found with the name: NAME”

      Use case resumes at step 1.

* 1d. Target already pinned

    * 1d1. AddressBook shows an error message. - “NAME is already the pinned person.”

      Use case ends.

Command Format

pin n/NAME

Example Commands

pin n/Yap Mei Ting

**Use case 7: Unpin a contact (Caregiver or Senior): unpin**

**MSS**

1. User unpins the currently pinned contact using the command "unpin"
2. AddressBook remove the pinned highlighted contact and displays the list accordingly.

   Use case ends.

**Extensions**

* 1a. No contact is currently pinned

    * 1a1. AddressBook shows an error message. - "No one is pinned."

      Use case resumes at step 1.

Command Format

unpin

Example Commands

unpin

### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2. Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be
   able to accomplish most of the tasks faster using commands than using the mouse.
4. The product should be for a single user i.e., (not a multi-user product).
5. The data should be stored locally and should be in a human editable text file.
6. DBMS should not be used to store data.
7. The software should work without requiring an installer.
8. The software should not depend on your own remote server.
9. The GUI should work well for, standard screen resolutions 1920x1080 and higher, and for screen scales 100% and 125%.
   In addition, the GUI should be usable (i.e., all functions can be used even if the user experience is not optimal)
   for, resolutions 1280x720 and higher, and for screen scales 150%.
10. Everything should be packaged into a JAR file.
11. The final product should not exceed 100MB and documents should not exceed 15MB/file.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Private contact detail**: A contact detail that is not meant to be shared with others
* **Caregiver**: A family member, helper, or close contact who provides day-to-day care for a senior.
* **Case Note**: A record of an interaction with a senior or caregiver (e.g., call, home visit, follow-up).
* **Emergency Contact**: A designated person to notify during emergencies, stored with name, relationship, and phone
  number.
* **Risk Tag**: A special tag indicating concerns such as `fall-risk`, `social-isolation`, or `memory-issues` to guide
  follow-ups.
* **Senior**: An elderly resident supported or engaged through AAC outreach activities.
* **Status**: The current state of a contact or case, such as `active`, `inactive`, `referred`, or `closed`.
* **Tag**: A keyword label assigned to a contact (e.g., `volunteer`, `caregiver`, `zone-west`) to enable filtering and
  grouping.
* **Volunteer**: A person assigned to support or accompany seniors for check-ins, activities, or emergencies.
* **Visit**: An in-person check-in with a senior, typically conducted at the senior’s home and recorded as a case note.
* **Human-Editable File**: The plain-text JSON file format used for storing data, viewable and editable without special
  tools.
* **Private contact detail**: Sensitive information (e.g., phone, address) that should not be shared with unauthorized
  users.
* **PDPA (Personal Data Protection Act)**: Singapore’s law governing the collection, use, and protection of personal
  data.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

    1. Download the jar file and copy into an empty folder

    1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be
       optimum.

1. Saving window preferences

    1. Resize the window to an optimum size. Move the window to a different location. Close the window.

    1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

    1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

    1. Test case: `delete 1`<br>
       Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message.
       Timestamp in the status bar is updated.

    1. Test case: `delete 0`<br>
       Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

    1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
       Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

    1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
