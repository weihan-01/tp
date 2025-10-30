---
  layout: default.md
  title: "User Guide"
  pageNav: 3
---

# Neighbourly User Guide

Neighbourly is a **desktop app for managing senior and caregiver contacts, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, Neighbourly can get your contact management tasks done faster than traditional GUI apps.

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/AY2526S1-CS2103-F13-4/tp/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your Neighbourly contact book.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar neighbourly.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add-snr n/Lim Ah Kow t/High Risk p/91234567 a/Blk 123 Bedok North Rd #02-45 n/Has dementia c/201` : Adds a senior contact named `Lim Ah Kow` to the address book.

   * `add-cgr n/Mei Hui p/98765432 a/Blk 620 Punggol Field Walk #08-23 n/Has experience with dementia caregiving` : Adds a caregiver contact named `Mei Hui` to the address book.

   * `delete s/1` : Deletes the senior at index 1.

   * `assign s/3 c/1`: Assigns the senior at index 3 to the caregiver at index 1 in the current list.

   * `unassign s/3 c/1`: Unassigns the caregiver at index 1 from the senior at index 3 in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<box type="info" seamless>

**Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add-cgr n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [a/ADDRESS]` can be used as `n/John Doe a/Blk 620 Punggol Field Walk #08-23` or as `n/John Doe`.

[//]: # (* Items with `…`​ after them can be used multiple times including zero times.<br>)

[//]: # (  e.g. `[t/TAG]…​` can be used as ` ` &#40;i.e. 0 times&#41;, `t/friend`, `t/friend t/family` etc.)

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.

</box>

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding a person: `add-snr`

Creates a new senior record in the system. In order to ensure seniors receive the care they require, each senior needs to maintain a phone and address contact as well as a risk tag. Each senior can also have additional medical notes and a caregiver assigned to them.

Format: `add-snr n/NAME t/RISK_TAG p/PHONE a/ADDRESS [nt/NOTES] [c/CAREGIVER_ID]`

<box type="tip" seamless>


**Tip:** A senior can either have a `Low Risk`, `Medium Risk` or `High Risk` tag

</box>

Examples:
* `add-snr n/Lim Ah Kow t/High Risk p/91234567 a/Blk 123 Bedok North Rd #02-45 nt/Has dementia c/201`
* `add-snr n/Tan Wei Ming t/Low Risk p/94567123 a/Blk 832 Punggol East #06-77`

### Adding a person: `add-cgr`

Creates a new caregiver record in the system. Each caregiver needs to maintain a phone number so caregiving organisations can reach out to ensure the senior they are assigned to has been contacted.

Format: `add-cgr n/NAME p/PHONE [a/ADDRESS] [nt/NOTES]`

Examples:
* `add-cgr n/Mei Hui p/98765432 a/Blk 620 Punggol Field Walk #08-23 nt/Has experience with dementia caregiving`
* `add-cgr n/Aaron Beng p/91234567 a/Blk 430 Clementi West Street 2 #10-10`

### Assigning a caregiver: `assign`

Assign a caregiver to a senior from the system.

Format: `assign s/SENIOR_INDEX c/CAREGIVER_INDEX`

Examples:
* `assign s/3 c/1`

### Unassigning a caregiver: `unassign`

Unassign a caregiver from a senior in the system.

Format: `unassign s/SENIOR_INDEX c/CAREGIVER_INDEX`

Examples:
* `unassign s/3 c/1`

### Listing all persons : `list`

Shows a list of all persons in the address book.

Format: `list`

### Locating persons by name: `find`

Finds persons whose names contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Only the name is searched.
* Only full words will be matched e.g. `Han` will not match `Hans`
* Persons matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `find John` returns `john` and `John Doe`
* `find alex david` returns `Alex Yeoh`, `David Li`<br>

  ![result for 'find command'](images/find-command.png)

### Filtering seniors by risk tag: `filter`

Shows Seniors whose risk tags match any of the provided tags.

Format: `filter t/TAG`

* Allowed tags: `lr`, `mr`, `hr` (input is case-insensitive; stored/compared in lowercase).
* Supplying an invalid tag (e.g., t/hello) results in an error.
* If no seniors match, the list becomes empty and the status shows 0 persons listed!

Examples:
* `filter t/lr` — shows all seniors tagged `lr`
* `filter t/MR` — accepted (case-insensitive); shows seniors tagged `mr`.
* `filter t/hello` — invalid (allowed: `lr`, `mr`, `hr`).

  ![result for 'filter t/hr'](images/filter-hr.png)

### Pin a senior/ caregiver by their index: `pin`

Pins maximum one senior and/ or one caregiver at the top of the respective panel display.

Format: `pin s/SENIOR_INDEX` or `pin c/CAREGIVER_INDEX`

* Only one person can be pinned at a time.
* Supplying an invalid senior or caregiver index will trigger an error.
* Attempting to pin multiple individuals in a single command will result in an error.
* If a senior or caregiver is already pinned, issuing a new pin command will replace the existing one. 
The previously pinned person will be automatically unpinned.

Examples:
* `pin s/1` — pins senior at senior index 1 
* `pin c/1` — pins caregiver at caregiver index 1
* `pin c/1 s/1` — invalid: only one person can be pinned at a time 

  ![result for 'pin s/1'](images/pin-senior-1.png)

### Unpin a senior/ caregiver or both by their index: `unpin`

Unpin a senior, a caregiver or both.
Changes will be reflected on the display panel.

Format: 
`unpin s` - Unpins the senior
`unpin c` - Unpins the caregiver
`unpin a` or `unpin` - Unpins person(s)

* Only the letters 's', 'c', or 'a' are allowed after unpin.
Inputs are not case-sensitive. 
* Any other characters or strings following unpin will trigger an error. 
* If no senior or caregiver is currently pinned, issuing any unpin command will result in an error.

Examples:
* `unpin s` or `unpin S` — unpin senior 
* `unpin c` or `unpin C` — unpin caregiver
* `unpin` or `unpin a` or `unpin A` — unpins senior and/ or caregiver 
* `unpin c/1` — invalid: incorrect format 

  ![result for 'unpin c'](images/unpin-caregiver.png)

### Deleting a person : `delete`

Deletes the specified senior/caregiver from the address book.

Format: `delete c/INDEX or s/INDEX`

* Deletes the senior/caregiver at the specified `INDEX`.
* The index refers to the index number shown in the displayed senior/caregiver list.
* The index **must be a positive integer** 1, 2, 3, …

Examples:
* `delete c/2` deletes the caregiver at index 2.
* `delete s/2` deletes the senior at index 2.

### Clearing all entries : `clear`

Clears all entries from the address book.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

Neighbourly data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

Neighbourly data are saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<box type="warning" seamless>

**Caution:**
If your changes to the data file makes its format invalid, Neighbourly will discard all data and start with an empty data file at the next run.  Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the Neighbourly to behave in unexpected ways (e.g., if a value entered is outside the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.

</box>

### Editing a person :

Edits information of a person

Format: `edit s/<SENIOR_ID> [n/NAME] [p/PHONE] [a/ADDRESS] [t/TAG]...` or
`edit c/<CAREGIVER_ID> [n/NAME] [p/PHONE] [a/ADDRESS] [t/TAG]...
`

* Edits the senior/caregiver based on the specified seniorId or caregiverId.
* The seniorId/caregiverId refers to the id number shown in the displayed senior/caregiver list.

Examples: 
* `edit s/1 n/John Tan p/91234567` Changes Senior at index 1 name to John Tan and phone to 91234567
* `edit c/2 n/Jane Lim` Changes Caregiver at index 2 name to Jane Lim

[//]: # (Edits an existing person in the address book.)

[//]: # ()
[//]: # (Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`)

[//]: # ()
[//]: # (* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​)

[//]: # (* At least one of the optional fields must be provided.)

[//]: # (* Existing values will be updated to the input values.)

[//]: # (* When editing tags, the existing tags of the person will be removed i.e adding of tags is not cumulative.)

[//]: # (* You can remove all the person’s tags by typing `t/` without)

[//]: # (    specifying any tags after it.)

[//]: # ()
[//]: # (Examples:)

[//]: # (*  `edit 1 p/91234567 e/johndoe@example.com` Edits the phone number and email address of the 1st person to be `91234567` and `johndoe@example.com` respectively.)

[//]: # (*  `edit 2 n/Betsy Crower t/` Edits the name of the 2nd person to be `Betsy Crower` and clears all existing tags.)

### Archiving data files `[coming in v1.6]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous Neighbourly home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

| Action                 | Format, Examples                                                                                                                                                                        |
|------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Add Senior**         | `add-snr n/NAME t/RISK_TAG p/PHONE a/ADDRESS [nt/NOTES] [c/CAREGIVER_ID]` <br> e.g., `add-snr n/Lim Ah Kow t/High Risk p/91234567 a/Blk 123 Bedok North Rd #02-45 n/Has dementia c/201` |
| **Add Caregiver**      | `add-cgr n/NAME p/PHONE [a/ADDRESS] [nt/NOTES]` <br> e.g., `add-cgr n/Mei Hui p/98765432 a/Blk 620 Punggol Field Walk #08-23 nt/Has experience with dementia caregiving`                |
| **Assign Caregiver**   | `assign s/SENIOR_INDEX c/CAREGIVER_INDEX` <br> e.g., `assign s/3 c/1`                                                                                                                   |
| **Unassign Caregiver** | `unassign s/SENIOR_INDEX c/CAREGIVER_INDEX` <br> e.g., `unassign s/3 c/1`                                                                                                               |
| **Edit**               | `edit s/SENIOR_ID [n/NAME] [p/PHONE] [a/ADDRESS] [t/TAG]` or `edit c/CAREGIVER_ID [n/NAME] [p/PHONE] [a/ADDRESS] [t/TAG]`<br> e.g., `edit c/2 n/Jane Lim`                               |
| **Filter**             | `filter t/TAG` <br> e.g., `filter t/hr` (shows all Seniors tagged `hr`; allowed tags: `lr`, `mr`, `hr`)                                                                                 |
| **Pin**                | `pin s/SENIOR_INDEX` or `pin c/CAREGIVER_INDEX` or `pin s/SENIOR_INDEX c/CAREGIVER_INDEX` <br> e.g., `pin s/1` or `pin c/1` (pins one senior or caregiver at the top of their list)     |
| **Unpin**              | `unpin s`, `unpin c`, or `unpin a` <br> e.g., `unpin s` (unpins senior), `unpin c` (unpins caregiver), `unpin` or `unpin a` (unpins all)                                                |
| **List**               | `list`                                                                                                                                                                                  |
| **Find**               | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`                                                                                                                              |
| **Delete**             | `delete s/<seniorId>` or `delete c/<caregiverId>` <br> e.g., `delete s/3`                                                                                                               |
| **Clear**              | `clear`                                                                                                                                                                                 |
| **Help**               | `help`                                                                                                                                                                                  |
