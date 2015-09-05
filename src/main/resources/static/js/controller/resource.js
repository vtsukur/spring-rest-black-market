var config =  require("../config.js"),
    rootUri = config.rootUri,
    prefix = config.prefix,
    JsonHalAdapter = require("traverson-hal"),
    traverson = require("traverson"),
    api;

traverson.registerMediaType(JsonHalAdapter.mediaType,
    JsonHalAdapter);

api = traverson.from(rootUri);

module.exports = {
    getRootHal: function (callback) {
        api.jsonHal()
            .follow(prefix + "ads")
            .getUri(function (err, uri) {
                if (err) {
                    console.log(err);
                    return;
                }
                callback(uri);
            });
    },
    getFields: function (fields, callback) {
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
    },
    getCurrentUser: function (callback) {
        api.jsonHal()
            .follow(prefix + "users", "search", prefix + "current-user")
            .getResource(function (err, res) {
                if (err) {
                    console.log(err);
                    return;
                }
                callback(res._links.self.href);
            });
    },
    getMyUri: function(callback) {
        api.jsonHal()
            .follow(prefix + "ads", "search", prefix + "my")
            .getUri(function (err, uri) {
                if (err) {
                    console.log(err);
                    return;
                }
                callback(uri);
            //    this.url = uri;
            //    this.fetch();
            });
    }
};