var reconcillitionFlag = 0;
$(function() {
    //var jsdata = JSON.parse(data);
    var table = $('#table-gl-data').DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength" : 25,
        "ajax": {
            url: myContextPath + "/accounts/executegl/list",
            data: function(data) {
                data.flag = reconcillitionFlag;
            }
        },
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            orderable: false,
            className: 'select-checkbox',
            "data": "stockNo",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input class="selectBox" type="checkbox" value="' + data + '">'
                }
                return data;
            }
        }, {
            targets: 1,
            "data": "stockNo"
        }, {
            targets: 2,
            "className" : "dt-right",
            "data": "PUR001_C",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 3,
            "className" : "dt-right",
            "data": "PUR001_D",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 4,
            "className" : "dt-right",
            "data": "COMM001_C",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 5,
            "className" : "dt-right",
            "data": "COMM001_D",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 6,
            "className" : "dt-right",
            "data": "TAX001_C",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 7,
            "className" : "dt-right",
            "data": "TAX001_D",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 8,
            "className" : "dt-right",
            "data": "REC001_C",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 9,
            "className" : "dt-right",
            "data": "REC001_D",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }

        }],
        "drawCallback": function(settings, json) {
            $('#table-gl-data').find('span.autonumber').autoNumeric('init')
        },
        "fnRowCallback": function(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
            if (aData.PUR001_C == "invalid") {
                $(nRow).find('td:eq(2)').css('background-color', '#FF9677');
            }
            if (aData.PUR001_D == "invalid") {
                $(nRow).find('td:eq(3)').css('background-color', '#FF9677');
            }
            if (aData.COMM001_C == "invalid") {
                $(nRow).find('td:eq(4)').css('background-color', '#FF9677');
            }
            if (aData.COMM001_D == "invalid") {
                $(nRow).find('td:eq(5)').css('background-color', '#FF9677');
            }
            if (aData.REC001_C == "invalid") {
                $(nRow).find('td:eq(8)').css('background-color', '#FF9677');
            }
            if (aData.REC001_D == "invalid") {
                $(nRow).find('td:eq(9)').css('background-color', '#FF9677');
            }
            if (aData.TAX001_C == "invalid") {
                $(nRow).find('td:eq(6)').css('background-color', '#FF9677');
            }
            if (aData.TAX001_D == "invalid") {
                $(nRow).find('td:eq(7)').css('background-color', '#FF9677');
            }
        }
    });

    $('#btn-glCountryRec').click(function() {
        reconcillitionFlag = 1;
        table.ajax.reload()
    })
    $('#glReconTwo').click(function() {
		reconcillitionFlag=2;
        table.ajax.reload()
    })

    // Date picker
    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    })
    function regex_escape(text) {
    	return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
    	};
    // Customize Datatable
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
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

})
