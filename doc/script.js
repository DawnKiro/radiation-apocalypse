function load() {
  let page = new URLSearchParams(window.location.search).get("page");
  if (!page) page = "index";
  setPage(page, true);
}

function setPage(page, replace = false) {
  let url = new URL(window.location);
  url.searchParams.set("page", page);
  if (replace) history.replaceState(null, "", url);
  else history.pushState(null, "", url);
  fetch("pages/" + page + ".txt")
    .then((response) => {
      if (response.ok) return response.text();
      else throw new Error(response.status);
    })
    .then((data) => {
      let splitIndex = data.indexOf("\n");
      let title = data.substring(0, splitIndex);
      let content = htmlOfContent(data.substring(splitIndex));
      document.title = "Documentation - " + title;
      if (page == "index") {
        document.body.children.item(0).innerHTML = "<span>Index</span>";
      } else {
        document.body.children.item(0).innerHTML =
          "<span onclick='setPage(&quot;index&quot;)'>Index</span> &gt; <span>" +
          title +
          "</span>";
      }
      document.body.children.item(1).innerHTML = content;
    })
    .catch((error) => {
      document.title = "Documentation - ERROR";
      document.body.children.item(1).innerHTML =
        "<p class=error>" + error + "</p>";
    });
}

function htmlOfContent(content) {
  let html = [];

  let elements = content
    .replaceAll("\n", "")
    .replaceAll("\r", "")
    .split("--")
    .filter((e) => e.length > 0);
  for (let element of elements) {
    let type = element.substring(0, 1);
    let content = element.substring(1).trim();
    switch (type) {
      case "p":
        html.push(tagOf("p", content));
        break;
      case "h":
        html.push(tagOf("h1", content));
        break;
      case "i":
        html.push("<img src=images/" + content + ".png>");
        break;
      case "l":
        let sections = content.split(";");
        html.push(
          "<h2 onclick='setPage(&quot;" +
            sections[0] +
            "&quot;)'>" +
            sections[1] +
            "</h2>",
        );
        break;
    }
  }

  return html.join("\n");
}

function tagOf(type, content) {
  return "<" + type + ">" + content + "</" + type + ">";
}

load();
window.addEventListener("popstate", (event) => load());
