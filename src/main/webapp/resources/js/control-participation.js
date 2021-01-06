var jlab = jlab || {};
jlab.check = function($td) {
    $td.html("\u2714");
};
jlab.uncheck = function($td) {
    $td.empty();
};
jlab.restore = function($td, checked) {
    if (checked) {
        jlab.check($td);
    } else {
        jlab.uncheck($td);
    }
};
jlab.toggle = function() {
    if (jlab.isRequest()) {
        window.console && console.log("Ajax already in progress");
        return;
    }

    jlab.requestStart();

    var $td = $(this),
            $tr = $td.closest("tr"),
            creditedControlId = $tr.attr("data-cc-id"),
            destinationId = $td.attr("data-destination-id"),
            checked = $td.html() !== null && ($.trim($td.html()) !== '');

    $td.html("<span class=\"button-indicator\"></span>");

    window.console && console.log("creditedControlId: " + creditedControlId + ", destinationId: " + destinationId);

    var request = jQuery.ajax({
        url: jlab.contextPath + "/ajax/toggle-control-participation",
        type: "POST",
        data: {
            creditedControlId: creditedControlId,
            destinationId: destinationId
        },
        dataType: "html"
    });

    request.done(function(data) {
        if ($(".status", data).html() !== "Success") {
            jlab.restore($td, checked);
            alert('Unable to toggle: ' + $(".reason", data).html());
        } else {
            if (checked) {
                jlab.uncheck($td);
            } else {
                jlab.check($td);
            }
        }

    });

    request.error(function(xhr, textStatus) {
        jlab.restore($td, checked);
        window.console && console.log('Unable to toggle: Text Status: ' + textStatus + ', Ready State: ' + xhr.readyState + ', HTTP Status Code: ' + xhr.status);
        alert('Unable to toggle');
    });

    request.always(function() {
        jlab.requestEnd();
    });
};
$(document).on("click", ".control-participation-table.editable td", function() {
    jlab.toggle.call(this);
});