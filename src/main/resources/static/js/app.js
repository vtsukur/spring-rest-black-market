switch (window.location.pathname) {
    case "/ui/index.html":
        require("./index.js")();
        break;
    case "/ui/admin.html":
        require("./admin.js")();
        break;
    default:
        break;
}
