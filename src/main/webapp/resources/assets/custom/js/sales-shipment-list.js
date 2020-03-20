$(function() {
    $(document).on('focus', '.select2-selection--single', function(e) {
        select2_open = $(this).parent().parent().siblings('select');
        select2_open.select2('open');
    });
    $(document).on('change', '.has-error', function() {
        $(this).find('input,select,textarea').valid();
    })
    
    // Customize Datatable
    $('#table-filter-search').keyup(function() {
    	table.search($(this).val()).draw();
    });
    $('#table-filter-length').change(function() {
    	table.page.len($(this).val()).draw();
    });

    $.getJSON(myContextPath + "/data/countryWithContinentName", function(data) {
        continentJson = data.data;
        $('#continent,.continentModal').select2({
            allowClear: true,
            width: '100%',
            data: $.map(continentJson, function(item) {
                return {
                    id: item.code,
                    text: item.name,
                    data: item
                };
            })
        }).val('').trigger('change');
    })
    $.getJSON(myContextPath + "/data/ports.json", function(data) {
        $('select[name="orginPort"],select[name="destPortSelect"]').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.portName,
                    text: item.portName
                };
            })
        }).val('').trigger('change');

    });
    $('#clone-container-location').cloneya({
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
        $('.select2').each(function() {
            if ($(this).data('select2')) {
                $(this).select2('destroy');
            }
        });

    }).on('after_append.cloneya', function(event, toclone, newclone) {
        $('.select2').select2({
            placeholder: function() {
                return $(this).attr('data-placeholder')
            },
            allowClear: true,

        })
        $('.datepicker').datepicker({
            format: "dd-mm-yyyy",
            autoclose: true
        }).on('change', function() {
            $(this).valid();

        })

    });

    // Date picker
    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    }).on('change', function() {
        $(this).valid();

    });

    //select2//

    $('.select2').select2({
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        width: '100%',
        allowClear: true,
    })

    // Countries/port Dropdowns /./.end
    var table = $('#table-shipment-list').DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": {
            url: myContextPath + "/shipping/schedule/list/data",
            data: function(data) {
//                 data.orginCountry = $('#searchCondition').find('select[name="orginCountry"]').val();
                data.orginPort = $('#searchCondition').find('select[name="orginPort"]').val();
//                 data.destCountrySelect = $('#searchCondition').find('select[name="destCountrySelect"]').val();
                data.destPortSelect = $('#searchCondition').find('select[name="destPortSelect"]').val();
//                 data.continent = $('#searchCondition').find('select[name="continent"]').val();
            },
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            orderable: false,
            className: 'select-checkbox',
            targets: 0
        }, {
            targets: 0,
            orderable: false,
            "visible": false,
            className: 'select-checkbox',
            "data": "id",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input class="selectBox" type="checkbox" data-stockno="' + row.stockNo + '" value="' + data + '">';
                }
                return data;
            }
        }, {
            targets: 1,
            "className": "details-control",
            "data": "shipmentCompany",

        }, {
            targets: 2,
            "className": "details-control",
            "data": "vesselName"
        }, {
            targets: 3,
            "className": "details-control",
            "data": "voyageNo"
        }, {
            targets: 4,
            "className": "details-control",
            "data": "deckHeight"
        }, {
            targets: 5,
            "className": "details-control",
            "data": "startPort"
        }, {
            targets: 6,
            "className": "details-control",
            "data": "startDate"
        }, {
            targets: 7,
            "className": "details-control",
            "data": "destinationPort"
        }, {
            targets: 8,
            "className": "details-control",
            "data": "endDate"
        }]

    });
    // Customize Datatable
    $('#table-filter-search').keyup(function() {
        table.search($(this).val()).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });
    table.on("click", "th.select-checkbox>input", function() {
        if (!$(this).is(':checked')) {
            table.rows({
                page: 'current'
            }).deselect();
            $("th.select-checkbox").removeClass("selected");
            table.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', false);

            });
        } else {
            table.rows({
                page: 'current'
            }).select();
            $("th.select-checkbox").addClass("selected");
            table.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', true);

            });
        }
    }).on("select", function() {
        if (table.rows({
            selected: true,
            page: 'current'
        }).count() !== table.rows({
            page: 'current'
        }).count()) {
            $("th.select-checkbox>input").removeClass("selected");
            $("th.select-checkbox>input").prop('checked', false);
        } else {
            $("th.select-checkbox>input").addClass("selected");
            $("th.select-checkbox>input").prop('checked', true);

        }

    }).on("deselect", function() {
        if (table.rows({
            selected: true,
            page: 'current'
        }).count() !== table.rows({
            page: 'current'
        }).count()) {
            $("th.select-checkbox>input").removeClass("selected");
            $("th.select-checkbox>input").prop('checked', false);
        } else {
            $("th.select-checkbox>input").addClass("selected");
            $("th.select-checkbox>input").prop('checked', true);

        }

    });
    $('#table-shipment-list tbody').on('click', 'td.details-control', function() {
        /*$(this).closest('tbody').find('.container-fluid').hide();*/

        var tr = $(this).closest('tr');

        var row = table.row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
        } else {
            table.rows('.shown').every(function() {
                // If row has details expanded
                if (this.child.isShown()) {
                    // Collapse row details
                    this.child.hide();
                    $(this.node()).removeClass('shown');
                }
            });
            row.child(formatview(row.data())).show();
            tr.addClass('shown');

        }

    });

    $(document).on('click', 'a.btn-delete', function() {
        if (!confirm($.i18n.prop('common.confirm.delete'))) {
            return false;
        }
        var tr = $(this).closest('tr');
        var row = table.row(tr);
        var scheduleId = row.data().scheduleId;
        deleteShipmentSchedule(scheduleId)
        row.remove();
        table.draw();
        var alertEle = $('#alert-block');
        $(alertEle).css('display', '').html($.i18n.prop('message.shipping.schedule.delete.success'));
        $(alertEle).fadeTo(5000, 500).slideUp(500, function() {
            $(alertEle).slideUp(500);
        });
    })
    $('#btn-search').on('click', function(event) {
        table.ajax.reload()
    })
})
function formatview(rowData) {

    var ifNotValid = function(val, str) {
        return typeof val === 'undefined' || val == null ? str : val;
    }

    var viewHTML = '<div class="container-fluid"><div class="box-body no-padding table-schedule-view"><div class="table-responsive"><table class="table table-bordered"><input type="hidden" name="id1" value="' + rowData.id + '"/>' + '<thead><tr>' + '<th>Loading Port</th>' + '<th>ETD</th>' + '<th>S/O Cut Date</th>' + '<th>Discharge Port</th>' + '<th>ETA</th>' +'<th>Sub Vessel</th>' + '</tr></thead><tbody></tbody></table></div></div></div>';
    let viewHTMLElement = $(viewHTML);
    let loadingPortArr = rowData.schedule.filter(function(item) {
        return item.portFlag == "loading";
    });
    let destinationPortArr = rowData.schedule.filter(function(item) {
        return item.portFlag == "destination";
    });
    let loadingPortLength = loadingPortArr.length;
    let destinationPortLength = destinationPortArr.length;
    let length = loadingPortLength >= destinationPortLength ? loadingPortLength : destinationPortLength;
    //loading port
    for (i = 0; i < length; i++) {
        let tr = '<tr>'
        tr += '<td>' + (i < loadingPortLength ? loadingPortArr[i].portName : "") + '</td>'
        tr += '<td>' + (i < loadingPortLength ? loadingPortArr[i].date : "") + '</td>'
        tr += '<td>' + (i < loadingPortLength ? ifNotValid(loadingPortArr[i].soCutDate, "") : "") + '</td>'
        tr += '<td>' + (i < destinationPortLength ? destinationPortArr[i].portName : "") + '</td>'
        tr += '<td>' + (i < destinationPortLength ? destinationPortArr[i].date : "") + '</td>'
        tr += '<td>' + (i < destinationPortLength ? destinationPortArr[i].subVessel : "") + '</td>'
        tr += '</tr>'
        $(tr).appendTo(viewHTMLElement.find('tbody'))
    }

    return viewHTMLElement.html();

}
function deleteShipmentSchedule(scheduleId) {
    var result;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        url: myContextPath + "/shipping/schedule/delete?scheduleId=" + scheduleId,
        contentType: "application/json",
        async: false,
        success: function(data) {
            result = data;
        }
    });
    return result;
}
