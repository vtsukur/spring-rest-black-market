var View = Backbone.View.extend({
    el: $(".container"),
    initialize: function () {
        _.bindAll(this, "render");
        this.model.bind("change", this.render);
    },
    render: function() {
        var $tbody = this.$("#ads-list tbody"),
            next = this.$("#next"),
            prev = this.$("#prev");
        $tbody.empty();
        _.each(this.model.embedded("ads"), function(data) {
            $tbody.append(new adView({model : data}).render().el);
        }, this);
        this.model.link("next") ? next.show() : next.hide();
        this.model.link("prev") ? prev.show() : prev.hide();
    },
    events: {
        "click #next": "goNext",
        "click #prev": "goPrev"
    },
    goNext: function(e) {
        e.preventDefault(e);
        this.go("next");
    },
    goPrev: function(e) {
        e.preventDefault(e);
        this.go("prev");
    },
    go: function(where){
        var data = URI(this.model.link(where).href()).query(true);
        ads.fetch({ data: data });
    }
});

var adView = Backbone.View.extend({
    tagName : "tr",
    template : _.template($("#ad-template").html()),
    render : function() {
        this.$el.html(this.template(this.model));
        return this;
    }
});

var AdsModel = Backbone.RelationalHalResource.extend({
    url: '/api/ads'
});

var ads = new AdsModel();
var view = new View({ model: ads }).render();
ads.fetch();