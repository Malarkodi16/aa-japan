var transporterJson, fromLocation, toLocation

$(function() {
    $.getJSON(myContextPath + "/data/transporters.json", function(data) {
        transportersJson = data;
        $('#transporter').select2({
            placeholder: "Select Transporter",
            allowClear: true,
            width: '100%',
            data: $.map(transportersJson, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        });
    })

    $.getJSON(myContextPath + "/data/locations.json", function(data) {
        locationJson = data;
        $('#from,#to').select2({
            allowClear: true,
            width: '100%',
            data: $.map(locationJson, function(item) {
                return {
                    id: item.code,
                    text: item.displayName
                };
            })
        })
        $('#from').val($('#from').attr('data-value')).trigger("change");
        $('#to').val($('#to').attr('data-value')).trigger("change");

    })

    $.getJSON(myContextPath + "/data/makerModel.json", function(data) {
        makersJson = data;
        var ele = $('#maker');
        $(ele).select2({
            allowClear: true,
            data: $.map(makersJson, function(item) {
                return {
                    id: item.name,
                    text: item.name,
                    data: item
                };
            })
        }).val($(ele).attr('data-value')).trigger("change");
        $('#model').val($('#model').attr('data-value')).trigger("change");
    })
    // Maker model
    $('#maker').on('change', function() {
        var data = $(this).select2('data');
        var modelEle = $('#model');
        $(modelEle).empty();
        $('#category').val('');
        if (data.length > 0 && !isEmpty(data[0].data)) {
            var modelList = data[0].data.models;
            $(modelEle).select2({
                allowClear: true,
                data: $.map(modelList, function(item) {
                    return {
                        id: item.modelName,
                        text: item.modelName,
                        data: item
                    };
                })
            }).val('').trigger("change");
        }

    });
    $('.select2').select2({
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        allowClear: true,
    });
    $('.model').select2({
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        allowClear: true,
    });
    function regex_escape(text) {
    	return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
    	};

    // Table Search Filter - Start
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });

    /* Transporter fFee Data-table*/

    var table_transport_fee_list_Ele = $('#table-transporter-fee-list');
    var table = table_transport_fee_list_Ele.DataTable({
       "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
       "pageLength" : 25,
        "ajax": myContextPath + "/transport/transporter/fee/list/datasource",
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "name": "Transporter",
            "data": "transporter",
        }, {
            targets: 1,
            "name": "From",
            "data": "from",
        }, {
            targets: 2,
            "name": "To",
            "data": "to",
        }, {
            targets: 3,
            "data": "transportCategory",
        }, {
            targets: 4,
            "className" : "dt-right",
            "data": "amount",
            "render": function(data, type, row) {
                var html = ''
                html += '<span class="autonumber" data-a-sign="&yen; " data-m-dec="0">' + row.amount + '</span>'
                return html;
            }
        }, {
            targets: 5,
            "name": "Action",
            "render": function(data, type, row) {
                var html = ''
                html += '<a href="' + myContextPath + '/transport/transporter/fee/edit/' + row.id + '" class="ml-5 btn btn-default btn-xs"><i class="fa fa-edit"></i></a> <a href="#" class="ml-5 btn btn-danger deleteButton btn-xs"><i class="fa fa-close"></i></a>'
                return html;
            }
        }, {
            targets: 6,
            "name": "Trasporter Id",
            "data": "transporterCode",
            "visible": false
        }, {
            targets: 7,
            "name": "From Id",
            "data": "fromCode",
            "visible": false
        }, {
            targets: 8,
            "name": "To Id",
            "data": "toCode",
            "visible": false
        }, ],
        "drawCallback": function(settings, json) {
            $('span.autonumber').autoNumeric('init')

        }

    });
    var transporterFilter;
    $('.transporter').change(function() {
        transporterFilter = $(this).val();
        table.draw();

    });

    var fromFilter;
    $('.from').change(function() {
        fromFilter = $(this).val();
        table.draw();

    });

    var toFilter;
    $('.to').change(function() {
        toFilter = $(this).val();
        table.draw();

    });

    var maker;
    $('.maker').change(function() {
        maker = $(this).val();
        table.draw();

    });

    var model = 0;
    $('.model').change(function() {
        model = $(this).val();
        table.draw();

    });

    $('#table-transporter-fee-list').on('click', '.deleteButton ', function() {
        if (!confirm('Do you want to delete?')) {
            return false;
        }
        var tr = $(this).closest('tr');
        var data = table.row(tr).data();
        var id = data.id;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "get",
            data: JSON.stringify(id),
            url: myContextPath + "/transport/transporter/fee/delete/" + id,
            contentType: "application/json",
            success: function(status) {
                table.row(tr).remove().draw();
            },
            error: function(status) {
                var alertEle = $('#alert-block').addClass('alert-danger');
                $(alertEle).removeClass('alert-success')
                $(alertEle).css('display', '').html('<strong>Warning!</strong> Transport fee is not deleted').fadeIn().delay(3000).fadeOut();
            }
        })
    })

    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        //Transporter Filter
        if (typeof transporterFilter != 'undefined' && transporterFilter.length != '') {
            if (aData[6].length == 0 || aData[6] != transporterFilter) {
                return false;
            }
        }
        //From Filter
        if (typeof fromFilter != 'undefined' && fromFilter.length != '') {
            if (aData[7].length == 0 || aData[7] != fromFilter) {
                return false;
            }
        }
        //To Filter
        if (typeof toFilter != 'undefined' && toFilter.length != '') {
            if (aData[8].length == 0 || aData[8] != toFilter) {
                return false;
            }
        }

        return true;
    });

    
    var addSupplierEle = $('#add-new-trn')
    $(addSupplierEle).on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }

    }).on('hide.bs.modal', function(e) {
        $(this).find('input').val('');
        $(this).find('span').html('')
    }).on('click', '#btn-create-trn', function() {
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return;
        }
        if (!$('#add-new-trn-form').find('input').valid()) {
            return false;
        } else {
            var data = {};
            data['name'] = $('#trn_name').val();
            data['code'] = "";
            $.ajax({
                beforeSend: function() {
                    $('#spinner').show()
                },
                complete: function() {
                    $('#spinner').hide();
                },
                type: "post",
                data: JSON.stringify(data),
                url: myContextPath + "/master/trn/save",
                contentType: "application/json",
                success: function(data) {
                }
            });
        }

    })
});


/*
$('#table-transporter-fee-list').on('click', '.deleteButton', function() {
    var itemId = $(this).closest('tr').find('input[name="itemId"]').val();
    var supplierCode = $('#table-master-supplier-list').find('input[name="supplierCode"]').val();

    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "get",
        //data: JSON.stringify(itemId),
        url: myContextPath + "/a/supplier/location/delete?itemId=" + itemId + "&supplierCode=" + supplierCode,
        contentType: "application/json",
        success: function(data) {
            console.log("deleted successfully");
            table_supplier_list.ajax.reload(); 
        }
    });
});*/
