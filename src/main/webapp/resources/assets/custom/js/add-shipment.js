let portJson = [];
$(function() {

//     $(document).on('focusout', 'input,select,textarea,.select2', function() {
//         $(this).closest('.element-wrapper').addClass('highlight');
//     });
//     $(document).on('blur', 'input,select,textarea,.select2', function() {
//         $(this).closest('.element-wrapper').removeClass('highlight');
//     })
    $(document).on('focus', '.select2-selection--single', function(e) {
        select2_open = $(this).parent().parent().siblings('select');
        select2_open.select2('open');
    });
    $(document).on('focusout', '.has-error', function() {
        $(this).find('input,select,textarea').valid();
    })

    $('.select2').select2({
        allowClear: true,
        width: '100%',
    }).val('').trigger('change');

    $('#clone-container-location.loading-port,#clone-container-location.destination-port').cloneya({
        minimum: 1,
        maximum: 999,
        cloneThis: '.clone-container-location-toclone',
        valueClone: false,
        dataClone: false,
        deepClone: false,
        cloneButton: '.row>.col-md-2>.btn-clone',
        deleteButton: '.row>.col-md-2>.btn-delete',
        clonePosition: 'after',
        serializeID: true,
        serializeIndex: true,

        preserveChildCount: true
    }).on('before_clone.cloneya', function(event, toclone) {
        $(toclone).find('select.select2').select2('destroy');

    }).on('after_append.cloneya', function(event, toclone, newclone) {
        $('.datepicker').datepicker({
            format: "dd-mm-yyyy",
            autoclose: true
        })
        $(toclone).find('select.select2').select2({
            allowClear: true
        });
        $(newclone).find('select.select2').select2({
            allowClear: true
        });
        //         $(newclone).find('.port.select2').empty();
        //         $(newclone).find('.country.select2').empty();
        var defaultDate = $(toclone).find('.schedule-date').val();
        if (!isEmpty(defaultDate)) {
            $(newclone).find('.schedule-date').datepicker('update', moment(defaultDate, 'DD-MM-YYYY').toDate());
        }
        $(newclone).find('input[name="schedule.portFlag"]').val($(toclone).find('input[name="schedule.portFlag"]').val())
        var continent = toclone.find('.continent').val();
        newclone.find('.continent').val(continent).trigger('change');

    });
    $.ajaxSetup({
        async: false
    });
    loadShipName($('#shipId'));

    $.getJSON(myContextPath + "/data/ports.json", function(data) {
        portJson = data;

    });

    let continentsElement = $('select[name="continents"]');

    continentsElement.change(function() {
        let val = $(this).val();
        let countryList = [];
        var portList = [];
        if (!isEmpty(val) && val.length > 0) {
            portList = portJson.filter(function(e) {
                return val.indexOf(e.continent) != -1;
            }, val);
        }

        var elements = $('select.port');
        $(elements).each(function(i) {
            let loopingElement = $(elements[i]);
            let loopingVal = loopingElement.val();
            loopingElement.empty();
            loopingElement.select2({
                allowClear: true,
                width: '100%',
                data: $.map(portList, function(item) {
                    return {
                        id: item.portName,
                        text: item.portName,
                        data: item
                    };
                })
            }).val(loopingVal).trigger('change');
        });

    })

    $.getJSON(myContextPath + "/data/countryWithContinentName", function(data) {
        continentJson = data.data;
        let selectedContinent = ["Asia"]
        continentsElement.select2({
            allowClear: true,
            width: '100%',
            tags: true,
            data: $.map(continentJson, function(item) {
                return {
                    id: item.name,
                    text: item.name,
                    data: item
                };
            })
        }).val(selectedContinent).trigger('change');
    });

    $('#clone-container-location.loading-port,#clone-container-location.destination-port').on('change', '.port', function() {
        var container = $(this).closest('.clone-container-location-toclone');
        container.find('.country').empty();
        var port = $(this).val();
        var selectedCountry;
        $.each(portJson, function(i, item) {
            if (item.portName == port) {
                selectedCountry = item.country;
                return false;
            }
        });
        $(this).closest('div').find('input[name="schedule.country"]').val(selectedCountry);

    })
    $.ajaxSetup({
        async: true
    });
    /*numbers*/
    $('.autonumber').autoNumeric('init');

    // Date picker
    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    })
    $('#clone-container-location').on('change', '.datepicker', function() {
        $(this).valid();
    })

    // Date picker
    $('div.loading-port').on('change', '.schedule-date', function() {
        let etd = moment($(this).val(), 'DD-MM-YYYY');
        let cutDate = moment(etd).add(-3, 'days').format('DD-MM-YYYY')
        $(this).closest('.clone-container-location-toclone').find('.soCutDate').datepicker('update', moment(cutDate, 'DD-MM-YYYY').toDate())
    });

    let modal_add_element = $('#modal-add-shipName')
    modal_add_element.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        // modalAddElementTriggerEle = $(event.relatedTarget);
    }).on('hidden.bs.modal', function() {
        $(this).find('span.help-block').html('')
        $(this).find('.has-error').removeClass('has-error')
        $(this).find('input,select').val('').trigger('change');
    }).on('click', 'button#add-ship', function() {
        /*if (!$('#modal-add-shipName form#formAddShipName').find('input').valid()) {
            return false;
        }*/
        let isValid = modal_add_element.find('form#formAddShipName').valid();
        if (!isValid) {
            return false;
        }

        let data = {};
        data['shipName'] = modal_add_element.find('input[name="name"]').val();
        console.log(data);
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/shipping/update/ShipName",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    $(modal_add_element).modal('toggle');
                    loadShipName($('#shipId'));
                }
            }
        });
    })
    //for edit
    var scheduleId = $('#scheduleId').val();
    if (!isEmpty(scheduleId)) {
        let response = editSchedule(scheduleId);

    }
    $('#btn-save').on('click', function() {
        if (!$('#addshipment').find('input,select').valid()) {
            return;
        }
    })
})
function updateFields(response) {
    data = response.data;
    var shippingCompanyNo = ifNotValid(data.shippingCompanyNo, '');
    var shipId = ifNotValid(data.shipId, '');
    var continents = ifNotValid(data.continents, '');
    //         var destinationPort = ifNotValid(data.destinationPort, '');
    var deckHeight = ifNotValid(data.deckHeight, '');
    var voyageNo = ifNotValid(data.voyageNo, '');
    $('select[name="shippingCompanyNo"]').val(shippingCompanyNo).trigger('change');
    $('select[name="shipId"]').val(shipId).trigger('change');
    //         $('select[name="destinationCountry"]').val(destinationCountry).trigger('change');
    //         $('select[name="destinationPort"]').val(destinationPort).trigger('change');
    $('select[name="continents"]').val(continents).trigger('change');

    $('input[name="deckHeight"]').val(deckHeight);
    $('input[name="voyageNo"]').val(voyageNo);
    for (var i = 0; i < data.schedule.length; i++) {
        let containerElement;
        let closestContainer;
        if (data.schedule[i].portFlag == "loading") {
            containerElement = $('#clone-container-location.loading-port');
            let lastButton = containerElement.find('button.btn-clone').last();
            closestContainer = lastButton.closest('div.row');
            if (data.schedule[i + 1].portFlag == "loading") {
                containerElement.find('button.btn-clone').last().click()
            }
        } else {
            containerElement = $('#clone-container-location.destination-port');
            let lastButton = containerElement.find('button.btn-clone').last();
            closestContainer = lastButton.closest('div.row');
            if (data.schedule[i + 1] != undefined) {
                containerElement.find('button.btn-clone').last().click()
            }
        }

        closestContainer.find('input[name="schedule.country"]').val(ifNotValid(data.schedule[i].country, ''));
        closestContainer.find('select[name="schedule.portName"]').val(ifNotValid(data.schedule[i].portName, '')).trigger('change');
        closestContainer.find('input[name="schedule.date"]').val(ifNotValid(data.schedule[i].date, ''));
        closestContainer.find('input[name="schedule.soCutDate"]').val(ifNotValid(data.schedule[i].soCutDate, ''));
        closestContainer.find('input[name="schedule.subVessel"]').val(ifNotValid(data.schedule[i].subVessel, ''));

    }
}

function loadShipName(element) {
    $.getJSON(myContextPath + "/data/shipname.json", function(data) {
        let shipJson = data;
        element.select2({
            allowClear: true,
            width: '100%',
            data: $.map(shipJson, function(item) {
                return {
                    id: item.shipId,
                    text: item.name
                };
            })
        }).val('').trigger('change');
    });
}

function editSchedule(scheduleId) {
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "get",
        async: true,
        url: myContextPath + '/shipping/schedule/info/' + scheduleId,
        contentType: "application/json",
        success: function(data) {
            updateFields(data)
        }
    });
}

let shipComp_add_element = $('#modal-add-shipComp')
shipComp_add_element.on('show.bs.modal', function(event) {
    if (event.namespace != 'bs.modal') {
        return;
    }
    // modalAddElementTriggerEle = $(event.relatedTarget);
}).on('hidden.bs.modal', function() {
    $(this).find('span.help-block').html('')
    $(this).find('.has-error').removeClass('has-error')
    $(this).find('input,select').val('').trigger('change');
}).on('click', 'button#add-shipComp', function() {
    let isValid = shipComp_add_element.find('form#formAddShipCompany').valid();
    if (!isValid) {
        return false;
    }
    let data = {};
    data['name'] = shipComp_add_element.find('input[name="name"]').val();
    data['shipCompAddr'] = shipComp_add_element.find('input[name="shipCompAddr"]').val();
    data['shipCompMail'] = shipComp_add_element.find('input[name="shipCompMail"]').val();
    data['mobileNo'] = shipComp_add_element.find('input[name="mobileNo"]').val();

    console.log(data);
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        data: JSON.stringify(data),
        url: myContextPath + "/shipping/create/ShipComp",
        contentType: "application/json",
        success: function(data) {
            if (data.status === 'success') {
                $(shipComp_add_element).modal('toggle');
                loadShipComp($('#shippingCompanyNo'));
            }
        }
    });
})
function loadShipComp(element) {
    $.getJSON(myContextPath + "/data/shippingCompany.json", function(data) {
        let shipJson = data;
        element.select2({
            allowClear: true,
            width: '100%',
            data: $.map(shipJson, function(item) {
                return {
                    id: item.shipId,
                    text: item.name
                };
            })
        }).val('').trigger('change');
    });
}
function checkVoyageNo() {
    var response;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        url: myContextPath + "/shipping/schedule/check/isVoyagenoExists",
        type: "get",
        cache: false,
        dataType: "json",
        async: false,
        data: {
            scheduleId: function() {
                return $('#scheduleId').val()
            },
            shippingCompanyNo: function() {
                return $('#shippingCompanyNo').val()
            },
            voyageNo: function() {
                return $('input[name="voyageNo"]').val()
            },
            shipId: function() {
                return $('select[name="shipId"]').val()
            }
        },

        success: function(data) {
            response = data;
        }

    })
    return response;

}
