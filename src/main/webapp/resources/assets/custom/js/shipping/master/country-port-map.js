let portJson = [];
$(function() {
    // Datatable Filter

    $('#table-filter-search').keyup(function() {
        table_countryPort_list.search($(this).val()).draw();
    });
    $('#table-filter-length').change(function() {
        table_countryPort_list.page.len($(this).val()).draw();
    });

    /* Port List Data-table */
    var table_countryPort_list_Ele = $('#table-master-countryPort-list');
    var table_countryPort_list = table_countryPort_list_Ele.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": myContextPath + "/master/country/port/list-data",
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "name": "Continent",
            "data": "continent",
        }, {
            targets: 1,
            "name": "Country",
            "data": "country",
        }, {
            targets: 2,
            "name": "Port",
            "data": "port",
        }, {
            targets: 3,
            "name": "YardDetails",
            "visible": false,
            "data": "yardDetails",
            "render": function(data, type, row) {
                return data.map(a=>a.yardName)
            }
        }, {
            targets: 4,
            "name": "Action",
            "render": function(data, type, row) {
                return '<a type="button" class="btn btn-primary ml-5 btn-xs" title="Edit Port" id="edit-port" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-edit-port"><i class="fa fa-fw fa-edit"></i></a>';
            }
        }]
    });

    $.getJSON(myContextPath + "/data/ports.json", function(data) {
        portJson = data;

    });

    var modelCountryPortMap = $('#modal-edit-port');
    var modalTriggerEle;
    modelCountryPortMap.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        modalTriggerEle = $(event.relatedTarget);
        var tr = $(modalTriggerEle).closest('tr');
        var rowData = table_countryPort_list.row($(modalTriggerEle).closest('tr')).data();
        $('input[name="country"]').val(rowData.country);
        $('.select2-tag').select2({
            placeholder: function() {
                return $(this).attr('data-placeholder')
            },
            allowClear: true,
            tags: true
        })
        $('select[name="port"]').select2({
            allowClear: true,
            width: '100%',
            data: $.map(portJson, function(item) {
                return {
                    id: item.portName,
                    text: item.portName
                };
            })
        }).val(rowData.port).trigger('change');

    }).on('hidden.bs.modal', function() {
        $(this).find('select').val('').trigger('change');
    }).on('click', 'button#save-port-map', function() {
        var rowData = table_countryPort_list.row($(modalTriggerEle).closest('tr')).data();
        row = table_countryPort_list.row($(modalTriggerEle).closest('tr'))
        var port = $('select[name="port"]').val();
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(port),
            url: myContextPath + "/master/country/port/saveMapped?country=" +rowData.country,
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    $(modelCountryPortMap).modal('toggle');
                    row.child.hide();
                    row.data(data.data).invalidate();

                }
            }
        });
    })

})
