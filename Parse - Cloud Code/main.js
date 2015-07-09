
Parse.Cloud.useMasterKey();

Parse.Cloud.beforeSave(Parse.User, function(request, response) {
    
    var action = request.object.op("follows");
    var message = undefined;
    var followedId = undefined;
     
    if(action !== undefined) {
        var affected;
        if ((affected = action.added()).length > 0) {
            followedId = affected[0].id;
            obtainUser(followedId, function (err, user) {
                if (err) response.error(JSON.stringify(affected) + " not found");
                else {
                    var relation = user.relation("followedBy");
                    var me = Parse.User.current();
                    relation.add(me);
                    user.save();
                    
                    //push
                    console.log(me.getUsername() + " is now following " + user.getUsername());
                    var message = me.getUsername() + " is now following you!";
                    var channels = [ getFeedChannel(user) ];
                    sendPush(channels, message, function (err) {
                       if (err) response.error(err);
                       else response.success(); 
                    });
                }
            });
        } else if ((affected = action.removed()).length > 0) {
            followedId = affected[0].id;
            obtainUser(followedId, function (err, user) {
               if (err) response.error(err);
                else {
                    var relation = user.relation("followedBy");
                    var me = Parse.User.current();
                    console.log(me.getUsername() + " is NOMORE following " + user.getUsername());
                    relation.remove(me);
                    user.save();
                    response.success();
                }
            });
        } else response.success();
    } else response.success();
});

Parse.Cloud.beforeSave("Tweets", function(request, response) {
    
    var me = Parse.User.current();
    var myFollowers = me.relation("followedBy");
    
    var channels = [];
    myFollowers.query().each(function(follower) {
        channels.push(getFeedChannel(follower));
    }, function () {
        var message = request.object.get("message");
        sendPush(channels, message, function (err) {
            if (err) response.error(err);
            else response.success();
        });
    });
});

function obtainUser(userId, callback) {
    var query = new Parse.Query(Parse.User);
    query.get(userId, {
        success: function (user) {
            callback(null, user);
        }, error: function (obj, err) {
            callback(err);
        }
    });
}

function getFeedChannel(user) {
    return "BT_" + user.id + "_feed";
}

function sendPush (channels, message, callback) {
    
    console.log("pushing " + message + " to: " + channels);
    Parse.Push.send({
        channels: channels,
        data: { alert: message },
        title: "Basic Tweet"
    }, {
        success: function() {
            callback(null);
        },
        error: function(error) {
            console.log("FAILED push to " + channels + " because: " + JSON.stringify(error));
            callback(error);
        }
    });
}
