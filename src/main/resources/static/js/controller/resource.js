var rootUri = "/",
    prefix = "currency-black-market:",
    JsonHalAdapter = require("traverson-hal"),
    traverson = require("traverson");

traverson.registerMediaType(JsonHalAdapter.mediaType,
    JsonHalAdapter);

var api = traverson.from(rootUri);

var Resource = function () {
    
};

Resource.prototype.getRootHal = function (callback) {
    api.jsonHal()
        .follow(prefix + "ads")
        .getUri(function (err, uri) {
            if (err) {
                console.log(err);
                return;
            }
            callback(uri);
        });
};

Resource.prototype.getFields = function (fields, callback) {
    var getField = function (fields, filedName) {
        return fields.filter(function (el) {
            return el.name === filedName;
        })[0];
    },
    getValue = function (doc) {
        return doc.map(function (el) {
            var value = el.trim();
            return {
                label: value,
                value: value
            };
        });
    };

    api.jsonHal()
        .follow("profile")
        .getResource(function (err, res) {
            if (err) {
                console.log(err);
                return;
            }
            res.descriptors.forEach(function (el) {
                $.ajax({ url: el.href })
                    .done(function (res) {
                        $.each(res.descriptors, function (key, value) {
                        if (value.id === "ad-representation") {
                            $.each(value.descriptors, function (key, value) {
                                if (value.name === "currency" || value.name === "type") {
                                    var field = getField(fields, value.name);
                                    field.options = getValue(value.doc.value.split(","));
                                }
                            });
                            callback(fields);
                        }
                    });
                });
            });
        });
};

Resource.prototype.getCurrentUser = function (callback) {
    api.jsonHal()
        .follow(prefix + "users", "search", prefix + "current-user")
        .getResource(function (err, res) {
            if (err) {
                console.log(err);
                return;
            }
            callback(res._links.self.href);
        });
};

module.exports = new Resource();