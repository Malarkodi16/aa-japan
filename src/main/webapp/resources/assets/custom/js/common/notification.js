var table_message_container;
$(function() {
    connectSockAndStomp();
    //initialConnectNotification();
//     $('#btn-save-shipping').on('click', function(event) {
//         sendAction(event);
//     })
    $("#notification-container .dropdown-menu").on('click', function(e) {
        e.stopPropagation();
    });
    updateNotificationCount()

    //table notifications
    var table_message_container_Ele = $('#messageContainer');
    table_message_container = table_message_container_Ele.DataTable({
        "dom": '<<t>>',
        "pageLength" : 25,
        "language": {
            "zeroRecords": " "
        },
        "bPaginate": false,
        "ajax": myContextPath + "/notifications/listOnLoad",
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            orderable: false,
            className: 'details-control',
            "data": "toUserFullname",
            "render": function(data, type, row) {
                var html = ''

                html = '<div class="container-fluid show-hand-cursor"><div class="row"><div class="col-md-12"><h5 class="' + (row.status == 0 ? 'font-bold' : 'font-normal') + '"> ' + row.toUserFullname + '<span data-action="' + row.actionUrl + '" class="action"> [' + row.message + ']</span></h5></div></div></div>';

                return html;
            }
        }/*, {
            targets: 1,
            orderable: false,
            className: 'details-control',
            "data": "createdDate"
        }*/
        ],

        "fnDrawCallback": function(oSettings) {
            $(oSettings.nTHead).hide();
        }
    });

    table_message_container.on('click', 'td.details-control', function(event) {
        var element = $(this);
        var data = table_message_container.row(element.closest('tr')).data();
        var result = updateNotificationReadStatus(data.id);
        if (data.actionUrl != '#') {
            window.location.replace(myContextPath + data.actionUrl);

        } else {
            var tr = element.closest('tr');
            var row = table_message_container.row(tr);
            row.data(result.data).invalidate();
            updateNotificationCount()
        }

    })

})

function sendNotification(event) {
    event.preventDefault();
    $.ajax({
        url: myContextPath + "/notifications/some-action",
        type: "POST"
    });
    return;
}

function notify(message) {
    var cloneElement = $('#cloneable-items').find('#messageContainer>li');

    var notificationsContainer = $('#notification-container');
    notificationsContainer.find('#unread-count').html(message.count)
    notificationsContainer.find('#count').html(message.totalCount)
    var notificationsItems = notificationsContainer.find('ul.menu');
    for (var i = 0; i < message.notifications.length; i++) {
        var element = cloneElement.clone();
        $(element).find(".username").html(ifNotValid(message.notifications[i].toUserFullname, ''));
        $(element).find(".message").html(ifNotValid(message.notifications[i].message, ''));
        if (data.notifications[i].status == 0) {
            $(element).find(".message").addClass("unread");
        } else {
            $(element).find(".message").addClass("read");
        }
        var actionUrl = message.notifications[i].actionUrl == "#" ? "#" : myContextPath + message.notifications[i].actionUrl
        $(element).find("a.action").attr('href', actionUrl).attr('data-json', JSON.stringify(message.notifications[i]));
        $(element).prependTo(notificationsItems);

    }
}
// function sendAction(event) {
//     event.preventDefault();
//     $.ajax({
//         url: myContextPath + "/invoice/shipping/order/save",
//         type: "POST"
//     });
//     return;
// }

function connectSockAndStomp() {
    // Create and init the SockJS object
    var socket = new SockJS(myContextPath + '/ws');
    var stompClient = Stomp.over(socket);
    // Subscribe the '/notify' channell
    stompClient.connect({}, function(frame) {
        stompClient.subscribe('/user/queue/notify', function(notification) {
            // Call the notify function when receive a notification
            console.log(JSON.parse(notification.body))
            var result = JSON.parse(ifNotValid(notification.body, {}));
            var notificationsContainer = $('#notification-container');
            notificationsContainer.find('#unread-count').html(ifNotValid(result.count, 0))
            notificationsContainer.find('#count').html(ifNotValid(result.totalCount, 0))
            //notify(JSON.parse(notification.body));
            table_message_container.ajax.reload();
        });
    });

    return;
}

function updateNotificationCount() {
    $.getJSON(myContextPath + "/notifications/getCounts", function(data) {
        var notificationsContainer = $('#notification-container');
        notificationsContainer.find('#unread-count').html(data.data.count)
        notificationsContainer.find('#count').html(data.data.notifications.length)
    })
}
function updateNotificationReadStatus(id) {
    var result;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "put",
        async: false,
        url: myContextPath + "/notifications/update?id=" + id,
        contentType: "application/json",
        success: function(data) {
            result = data;

        }
    })
    return result;
}
