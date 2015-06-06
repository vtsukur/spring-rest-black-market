/**
 * Created by my8bit on 6/6/15.
 */
traverson.registerMediaType(TraversonJsonHalAdapter.mediaType,
    TraversonJsonHalAdapter);

var rootUri = '/';
var api = traverson.from(rootUri);
api.jsonHal()
    .follow('ads', 'search') // works without 'search'
    .getResource(function(err, resource) {
        if (err) {
            console.log(err);
            return;
        }
        console.log(JSON.stringify(resource, null, 2));
    });

