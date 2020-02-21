## Web Browser Project

#### Contributors:
- Preston __
- Logan __
- Stephen Beckstrand

---

## User Interface

### Top: Tool Bar

- [ ] Address Bar
- [ ] Back Button
- [ ] Forward Button
- [ ] Refresh Button
- [ ] Home Button
- [ ] Drop Down Menu
  - [ ] New Tab (if tabs are implemented)
  - [ ] Settings
  - [ ] History (if implemented)
  - [ ] Bookmarks
  - [ ] About
- [ ] Tabs (if implemented)

#### General Detail

To store details used by our browser, we would need to save the following data files to user's system:
- Settings Data
  - Home page
  - Default search engine
  - History retention (if history is implemented)
- Bookmarks
- History (if history is implemented)
- Live History (Path to current page for forward and back buttons)

#### Address Bar

If protocol (http or https) is not included in input:
- [ ] Check if Fully Qualified Domain Name (FQDN) and attent to resolve the hostname defaulting with HTTP.
- [ ] If not FQDN, have address bar query request into default search engine.

If History is implimented, we coud look into auto-filling results as being typed into address bar. Might be a bit of work considering we would need to store history in a way that allows for being searched. I would consider this a low priority compared to other items.

#### Back Button + Forward Button

Fairly straight forward, we would need a file that contains detail on our current page, and the pages that lead up to it. A linked list would probably work best here.

#### Settings

From this page the user would be able to set their home page, default search engine and history retention.

#### Bookmarks

Should provide a list of all current bookmarks with the ability to remove them. We could add a button to add a bookmark where they would provide the URL and and name of bookmark. If we implement tabs, we could have it allow them to select the URL already live in a tab.

In general, we will need a shortcut to save a bookmark from a web page (Ctrl + Shift + B is used by popular browsers). This should give a prompt to add bookmark name and by default set to current URL.

#### About

Details about the browser, its creators and a link to the repository where code is stored.


## Above and Beyond stuff

Caching
easter eggs
tabs
History
View Page Source
Developer Tools
- Javascript output
- assets loaded
