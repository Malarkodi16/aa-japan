var countriesJson;
$(function() {
    $(document).on('focus', '.select2-selection--single', function(e) {
        select2_open = $(this).parent().parent().siblings('select');
        select2_open.select2('open');
    });
    $(document).on('change', '.has-error', function() {
        $(this).find('input,select,textarea').valid();
    })
  //set status
    setShippingDashboardStatus();
    //Country Json
    $.getJSON(myContextPath + "/data/countries.json", function(data) {
        countriesJson = data;
        var elements = $('.country-dropdown');
        $('.country-dropdown').select2({
            allowClear: true,
            width: '100%',
            data: $.map(countriesJson, function(item) {
                return {
                    id: item.country,
                    text: item.country
                };
            })
        }).val('').trigger('change');
    })

    //Forwarder Json
    $.getJSON(myContextPath + "/data/forwarders.json", function(data) {
        $('#forwarderFilter').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        }).val('').trigger("change");
    });
    $('.select2').select2({
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        allowClear: true,
    });
    //Shipping Company Json
    $.getJSON(myContextPath + "/shipping/data/shipsWithCompanyName", function(data) {
        shippingJson = data.data;
        var elements = $('#shipping-company-filter');
        $('#shipping-company-filter').select2({
            allowClear: true,
            width: '100%',
            data: $.map(shippingJson, function(item) {
                return {
                    id: item.shippingCompanyNo,
                    text: item.shippingCompanyName,
                    data: item
                };
            })
        }).val('').trigger('change');
    })

    //Port Filter 
    var requestFromSalesPortFilterELe = $('#port-filter-from-sales');
    requestFromSalesPortFilterELe.select2({
        allowClear: true,
        width: '100%',
    }).on('change', function() {
        port = $(this).val();
    })

    //Ship Filter
    var shippingRequestedShipFilterELe = $('#ship-filter');
    shippingRequestedShipFilterELe.select2({
        allowClear: true,
        width: '100%',
    }).on('change', function() {
        port = $(this).val();
        //table_shipping_requested.draw();
    })

    //Country based Port Filter
    $('#country-filter-from-sales').on('change', function() {
        var val = ifNotValid($(this).val(), '');
        requestFromSalesPortFilterELe.empty();
        if (!isEmpty(val)) {
            var data = filterOneFromListByKeyAndValue(countriesJson, "country", val);
            if (data != null) {
                requestFromSalesPortFilterELe.select2({
                    allowClear: true,
                    width: '100%',
                    data: $.map(data.port, function(item) {
                        return {
                            id: item,
                            text: item
                        };
                    })
                }).val('').trigger('change');
            }

        }
        country = val;
    });

    //Shipping Company Based Ship Filter
    $('#shipping-company-filter').on('change', function() {
        shippingRequestedShipFilterELe.empty()
        var data = $(this).select2('data');
        if (typeof data[0].data == 'undefined') {
            return;
        }

        $('#ship-filter').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data[0].data.items, function(item) {
                return {
                    id: item.shipId,
                    text: item.name

                };
            })
        }).val('').trigger('change');
    })
    //Search Button
    $('#btn-searchData').click(function() {
        table_shipping_requested.ajax.reload()
    })

    var ifNotValid = function(val, str) {
        return typeof val === 'undefined' || val == null ? str : val;
    }

    //table shipping status
    var table_shipping_requested_Ele = $('#table-shipping-requested');
    var table_shipping_requested = table_shipping_requested_Ele.DataTable({
       "dom": '<ip<t>ip>',
       "pageLength" : 25,
        "ajax": {
            url: myContextPath + "/shipping/requested/status/datasource",
            data: function(data) {
                data.forwarderFilter = ifNotValid($('#forwarderFilter').find('option:selected').val(), '');
                data.countryFilter = ifNotValid($('#country-filter-from-sales').find('option:selected').val(), '');
                data.portFilter = ifNotValid($('#port-filter-from-sales').find('option:selected').val(), '');
                data.shipmentTypeFilter = ifNotValid($('#shipmentTypeFilter').find('option:selected').val(), '');
                data.shipCompanyFilter = ifNotValid($('#shipping-company-filter').find('option:selected').val(), '');
                data.shipFilter = ifNotValid($('#ship-filter').find('option:selected').val(), '');

            }
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            orderable: false,
            className: 'details-control',
            "data": "vessel",
            "render": function(data, type, row) {
                return '<div class="container-fluid"><div class="row"><div class="col-md-12"><h5 class="font-bold">' + row.shippingCompanyName + ' - ' + row.vessel + ' [' + row.voyageNo + ']<i class="fa fa-plus-square-o pull-right"></i></h5></div></div></div>';
            }
        }],
        "fnDrawCallback": function(oSettings) {
            $(oSettings.nTHead).hide();
        }

    });
    //expand details
    table_shipping_requested.on('click', 'td.details-control', function() {
        var tr = $(this).closest('tr');
        var row = table_shipping_requested.row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
        } else {
            table_shipping_requested.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
                var row = table_shipping_requested.row(rowIdx);
                if (row.child.isShown()) {
                    row.child.hide();
                    tr.removeClass('shown');
                }

            })
            row.child(format(row.data())).show();
            tr.addClass('shown');
        }
    });
})
function format(rowData) {
    var element = $('#shipping-requested-details-view>.detail-view').clone();
    var tbody = '';
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    for (var i = 0; i < rowData.items.length; i++) {
        var row = $(rowClone).clone();
        /* let status = ""
	          , className = "";
	        if (ifNotValid(rowData.items[i].status, '') == 1) {
	            $(row).find('td.status>span.label').addClass('label-default');
	            $(row).find('td.status>span.label').html('In Transit');
	        }*/
        var status;
        var className;
        if (rowData.items[i].status == 1) {
            status = "In Transit"
            className = "label-success"
        }
        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.stockNo').html(ifNotValid(rowData.items[i].stockNo, ''));
        $(row).find('td.chassisNo').html(ifNotValid(rowData.items[i].chassisNo, ''));
        $(row).find('td.shipmentType').html(ifNotValid(rowData.items[i].shippingType, '') == 1 ? 'RORO' : ifNotValid(rowData.items[i].shippingType, '') == 2 ? 'CONTAINER' : '');
        $(row).find('td.forwarder').html(ifNotValid(rowData.items[i].forwarder, ''));
        $(row).find('td.etd').html(ifNotValid(rowData.items[i].etd, ''));
        $(row).find('td.eta').html(ifNotValid(rowData.items[i].eta, ''));
        $(row).find('td.status>span').addClass(className).html(status);
        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);

    }

    return element;
}
