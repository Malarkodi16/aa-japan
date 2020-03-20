let countryJson = [];
$(function() {
    // Datatable Filter 

    $('#table-filter-search').keyup(function() {
    	table_port_list.search($(this).val()).draw();
    });
    $('#table-filter-length').change(function() {
    	table_port_list.page.len($(this).val()).draw();
    });

    /* Port List Data-table*/
    var table_port_list_Ele = $('#table-master-port-list');
    var table_port_list = table_port_list_Ele.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": myContextPath + "/master/port/list-data",
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "name": "Company",
            "data": "continent",
        }, {
            targets: 1,
            "name": "Address",
            "data": "country",
        }, {
            targets: 2,
            "name": "Phone",
            "data": "portName",
        }]
    });

    $('.select2').select2({
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        width: '100%',
        allowClear: true,
    });

    let continentsElement = $('select[name="continent"]');
    $.getJSON(myContextPath + "/data/countryWithContinentName", function(data) {
        continentJson = data.data;
        continentsElement.select2({
            allowClear: true,
            width: '100%',
            data: $.map(continentJson, function(item) {
                return {
                    id: item.name,
                    text: item.name,
                    data: item
                };
            })
        }).val('').trigger('change');
    });
    $.getJSON(myContextPath + "/data/countries.json", function(data) {
        countryJson = data;

    });
    continentsElement.change(function() {
        let val = $(this).val();
        let countryList = [];
        if (!isEmpty(val) && val.length > 0) {
            countryList = countryJson.filter(function(e) {
                return val.indexOf(e.continent) != -1;
            }, val);
        }

        var elements = $('select[name="country"]').empty();
        elements.select2({
            allowClear: true,
            width: '100%',
            data: $.map(countryList, function(item) {
                return {
                    id: item.country,
                    text: item.country
                };
            })
        }).val('').trigger('change');
    });
    let modal_add_port = $('#modal-add-port')
    modal_add_port.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        // modalAddElementTriggerEle = $(event.relatedTarget);
    }).on('hidden.bs.modal', function() {
        $(this).find('input,select').val('').trigger('change');
    }).on('click', '#save-port',function() {
        if (!$("#port-form").valid()) {
            return false;
        }
        var data = getFormData($("#port-form").find('input,select'));
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: 'post',
            data: data,
            url: myContextPath + "/master/port/save",
            async: true,
            dataType: "json",
            success: function(data) {
                table_port_list.ajax.reload();
                $('#modal-add-port').modal('toggle');
                $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Port Saved.');
                $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                    $("#alert-block").slideUp(500);
                });

            }
        });
    })
})
