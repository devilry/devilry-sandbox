Ext.require(['Ext.data.*', 'Ext.grid.*']);

Ext.define('Person', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',
        type: 'int',
        useNull: true
    }, 'email', 'first', 'last'],

    validations: [{
        type: 'length',
        field: 'email',
        min: 1
    }, {
        type: 'length',
        field: 'first',
        min: 1
    }, {
        type: 'length',
        field: 'last',
        min: 1
    }],


    proxy: {
        type: 'rest',
        url: 'user/',
        reader: {
            type: 'json',
            root: 'data'
        },
        writer: {
            type: 'json'
        }
    }
});

Ext.onReady(function(){

    var store = Ext.create('Ext.data.Store', {
        autoLoad: true,
        autoSync: true,
        model: 'Person',
        listeners: {
            write: function(store, operation){
                var record = operation.getRecords()[0],
                    name = Ext.String.capitalize(operation.action),
                    verb;
                    
                    
                if (name == 'Destroy') {
                    record = operation.records[0];
                    verb = 'Destroyed';
                } else {
                    verb = name + 'd';
                }
                //console.log(record);
                var msg = Ext.String.format("{0} user: {1}", verb, record.getId());
                console.log(msg);
            }
        }
    });
    
    var rowEditing = Ext.create('Ext.grid.plugin.RowEditing');
    
    var grid = Ext.create('Ext.grid.Panel', {
        renderTo: 'table-ct',
        plugins: [rowEditing],
        width: 400,
        height: 300,
        frame: true,
        title: 'Users',
        store: store,
        iconCls: 'icon-user',
        columns: [{
            text: 'ID',
            width: 40,
            sortable: true,
            dataIndex: 'id'
        }, {
            text: 'Email',
            flex: 1,
            sortable: true,
            dataIndex: 'email',
            field: {
                xtype: 'textfield'
            }
        }, {
            header: 'First',
            width: 80,
            sortable: true,
            dataIndex: 'first',
            field: {
                xtype: 'textfield'
            }
        }, {
            text: 'Last',
            width: 80,
            sortable: true,
            dataIndex: 'last',
            field: {
                xtype: 'textfield'
            }
        }],
        dockedItems: [{
            xtype: 'toolbar',
            items: [{
                text: 'Add',
                iconCls: 'icon-add',
                handler: function(){
                    // empty record
                    store.insert(0, new Person());
                    rowEditing.startEdit(0, 0);
                }
            }, '-', {
                itemId: 'delete',
                text: 'Delete',
                iconCls: 'icon-delete',
                disabled: true,
                handler: function(){
                    var selection = grid.getView().getSelectionModel().getSelection()[0];
                    if (selection) {
                        store.remove(selection);
                    }
                }
            }]
        }]
    });
    grid.getSelectionModel().on('selectionchange', function(selModel, selections){
        grid.down('#delete').setDisabled(selections.length === 0);
    });











    Ext.override(Ext.form.action.Submit, {
        run: function() {
            //console.log('Hello world');
            //console.log(this);
            //console.log(this.form.getValues());
            var values = this.form.getValues();
            var user = Ext.ModelManager.create(values, 'Person');
            user.save({
                success: this.onSuccess,
                failure: this.onFailure,
                scope: this,
                timeout: (this.timeout * 1000) || (this.form.timeout * 1000),
            });
        }
    });


    /***************************
     * FORM
     **************************/
    Ext.create('Ext.form.Panel', {
        title: 'Simple Form',
        renderTo: 'form-ct',
        bodyPadding: 5,
        width: 350,

        // The form will submit an AJAX request to this URL when submitted
        //url: 'user/',

        // Fields will be arranged vertically, stretched to full width
        layout: 'anchor',
        defaults: {
            anchor: '100%'
        },

        // The fields
        defaultType: 'textfield',
        items: [{
            fieldLabel: 'First Name',
            name: 'first',
            allowBlank: false
        },{
            fieldLabel: 'Last Name',
            name: 'last',
            allowBlank: false
        },{
            fieldLabel: 'Email',
            name: 'email',
            allowBlank: false
        }],

        // Reset and Submit buttons
        buttons: [{
            text: 'Reset',
            handler: function() {
                this.up('form').getForm().reset();
            }
        }, {
            text: 'Submit',
            //formBind: true, //only enabled once the form is valid
            //disabled: true,

            handler: function() {
                this.up('form').getForm().submit({
                    url: 'user/',
                    method: 'POST',
                    submitEmptyText: true,
                    waitMsg: 'Saving Data...'
                });
            }


            //handler: function() {
                //var form = this.up('form').getForm();
                //if (form.isValid()) {
                    //form.submit({
                        //success: function(form, action) {
                            //Ext.Msg.alert('Success', action.result.msg);
                        //},
                        //failure: function(form, action) {
                            //Ext.Msg.alert('Failed', action.result.msg);
                        //}
                    //});
                //}
            //}
        }]
    });


});
