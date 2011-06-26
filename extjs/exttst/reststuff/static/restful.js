Ext.require(['Ext.data.*', 'Ext.grid.*']);

Ext.define('Person', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'id', type: 'int', useNull: true},
        'email', 'first', 'last',
        {name: 'score', type: 'int'}],

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












/* Override the method that is used to submit forms. */
Ext.override(Ext.form.action.Submit, {
    run: function() {
        var values = this.form.getValues();
        var obj = Ext.ModelManager.create(values, this.form.model);
        obj.save({
            form: this.form,
            success: this.onSuccess,
            failure: this.onFailure,
            scope: this,
            timeout: (this.timeout * 1000) || (this.form.timeout * 1000),
        });
    },

    onSuccess: function(record, operation) {
        this.record = record;
        this.operation = operation;
        this.form.afterAction(this, true);
    },

    onFailure: function(record, operation){
        this.record = record; // Always null?
        this.operation = operation;
        this.response = operation.response;
        this.form.markInvalid(operation.responseData.errors);

        if(operation.error.status === 0) {
            this.failureType = Ext.form.action.Action.CONNECT_FAILURE;
        } else if(operation.error.status >= 400 && operation.error.status < 500) {
            this.failureType = Ext.form.action.Action.SERVER_INVALID;
        } else {
            this.failureType = Ext.form.action.Action.LOAD_FAILURE;
        }
        this.form.afterAction(this, false);
    }
});


/* Since ExtJS for some reason goes into panic mode for any HTTP status
 * code except 200 (and ignores the response text), we need to override
 * setException in the REST proxy and manually decode the responseText.
 * (http://www.sencha.com/forum/showthread.php?135143-RESTful-Model-How-to-indicate-that-the-PUT-operation-failed&highlight=store+failure)
 *
 * However how do we get this into the form when we do not have any link to the form?
 *      - We add the response and the the decoded responsedata to the
 *        operation object, which is available to onFailure in Submit.
 * */
Ext.override(Ext.data.proxy.Rest, {
    setException: function(operation, response){
        operation.response = response;
        operation.responseText = response.responseText;
        operation.responseData = Ext.JSON.decode(operation.responseText); // May want to use a Reader
        operation.setException({
            status: response.status,
            statusText: response.statusText
        });
    },
});












function formExample() {
    /***************************
     * FORM
     **************************/
    Ext.create('Ext.form.Panel', {
        title: 'Simple Form',
        renderTo: 'form-ct',
        bodyPadding: 5,
        width: 350,
        model: 'Person',

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
        },{
            fieldLabel: 'Score',
            name: 'score',
            allowBlank: false
        }],

        // Reset and Submit buttons
        buttons: [{
            text: 'Submit',
            //formBind: true, //only enabled once the form is valid
            //disabled: true,

            handler: function() {
                this.up('form').getForm().submit({
                    submitEmptyText: true,
                    waitMsg: 'Saving Data...'
                    //failure: function(form, action) {
                        //if (action.failureType === Ext.form.action.Action.CONNECT_FAILURE) {
                            //Ext.Msg.alert('Error', 'Connection failed');
                        //} else if (action.failureType === Ext.form.action.Action.SERVER_INVALID) {
                            //Ext.Msg.alert('Error', action.operation.error.statusText);
                        //} else {
                            //Ext.Msg.alert('Server ERROR', Ext.String.format("{0}: {1}",
                                //action.operation.error.status,
                                //action.operation.error.statusText));
                        //}
                    //}
                });
            }
        }]
    });
}




function editableTableExample() {
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
                var msg = Ext.String.format("{0} user: {1}", verb, record.getId());
                console.log(msg);
            },

            //exception: function(proxy, response, operation) {
                //console.log('exception');
                //Ext.MessageBox.show({
                    //title: 'Error',
                    //msg: operation.getError(),
                    //icon: Ext.MessageBox.ERROR,
                    //buttons: Ext.Msg.OK
                //});
            //}
        }
    });
    
    var rowEditing = Ext.create('Ext.grid.plugin.RowEditing', {
        errorSummary: true
    });
    
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
        }, {
            text: 'Score',
            width: 80,
            sortable: true,
            dataIndex: 'score',
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
}




function chartExample() {
    Ext.create('Ext.chart.Chart', {
       renderTo: 'chart-example',
       width: 400,
       height: 300,
       store: store
    });
}



Ext.onReady(function(){
    formExample();
    editableTableExample();
});
