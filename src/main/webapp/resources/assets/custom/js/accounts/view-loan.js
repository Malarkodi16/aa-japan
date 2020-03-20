

 var table = $('#table-viewloan').DataTable({

        "dom": '<<t>ip>',
        "pageLength" : 25,
        "ajax": myContextPath + "/accounts/viewloanlist-data",
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
            "searchable": false,
            className: 'select-checkbox',
            "name": "id",
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
            orderable: false,
            "searchable": true,
            "className": "details-control",
            "name": "sequence",
            "data": "sequence",

        }, {
            targets: 2,
            orderable: false,
            "className": "details-control",
            "searchable": true,
            "name": "date",
            "data": "date"
        }, {
            targets: 3,
            orderable: false,
            "className": "details-control",
            "searchable": true,
            "name": "bank",
            "data": "bank"
        }, {
            targets: 4,
            orderable: false,
            "className": "details-control",
            "searchable": true,
            "name": "reference",
            "data": "reference"
        },
        {
            targets: 5,
            orderable: false,
            "className": "details-control",
            "searchable": true,
            "name": "loantype",
            "data": "loantype"
        },
        {
            targets: 6,
            orderable: false,
            "className": "details-control",
            "searchable": true,
            "name": "duedate",
            "data": "duedate"
        },
        {
            targets: 7,
            orderable: false,
            "className": "details-control",
            "searchable": true,
            "name": "fromdate",
            "data": "fromdate"
        },
        {
            targets: 8,
            orderable: false,
            "className": "details-control",
            "searchable": true,
            "name": "todate",
            "data": "todate"
        },
        {
        targets: 9,
        orderable: false,
        "className": "details-control",
        "searchable": true,
        "name": "interestrate",
        "data": "interestrate"
    },
    {
    targets: 10,
    orderable: false,
    "className": "details-control",
    "searchable": true,
    "name": "loanterm",
    "data": "loanterm"
},
{
    targets: 11,
    orderable: false,
    "className": "details-control",
    "searchable": true,
    "name": "description",
    "data": "description"
},
{
    targets: 12,
    orderable: false,
    "className": "details-control",
    "searchable": true,
    "name": "loanamount",
    "data": "loanamount"
},
{
    targets: 13,
    orderable: false,
    "className": "details-control",
    "searchable": true,
    "name": "loanupdate",
    "data": "loanupdate"
}]

    });