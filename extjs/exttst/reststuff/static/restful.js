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

Ext.define('FieldError', {
    extend: 'Ext.data.Model',
    fields: ['id', 'msg']
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






    /* Override the method that is used to submit forms. */
    Ext.override(Ext.form.action.Submit, {
        run: function() {
            var values = this.form.getValues();
            var user = Ext.ModelManager.create(values, this.form.model);
            user.save({
                form: this.form,
                success: this.onSuccess,
                failure: this.onFailure,
                scope: this,
                afterRequest: this.afterReq,
                timeout: (this.timeout * 1000) || (this.form.timeout * 1000),
            });
        },

        onSuccess: function(record, operation) {
            console.log("Success");
            this.record = record;
            this.operation = operation;
            var response = operation.response;

            var form = this.form,
                success = true,
                result = this.processResponse(response);
            if (result !== true && !result.success) {
                if (result.errors) {
                    form.markInvalid(result.errors);
                }
                this.failureType = Ext.form.action.Action.SERVER_INVALID;
                success = false;
            }
            form.afterAction(this, success);
        },

        onFailure: function(record, operation){
            console.log("Failure");
            this.record = record; // Always null?
            this.operation = operation;
            this.response = operation.response;

            this.result = this.handleResponse(this.response);

            if(operation.error.status === 0) {
                this.failureType = Ext.form.action.Action.CONNECT_FAILURE;
            } else if(operation.error.status >= 400 && operation.error.status < 500) {
                this.failureType = Ext.form.action.Action.SERVER_INVALID;
            } else {
                this.failureType = Ext.form.action.Action.LOAD_FAILURE;
            }
            this.form.afterAction(this, false);
        },

        handleResponse: function(response) {
            console.log('handleResponse');
            var form = this.form,
                errorReader = form.errorReader,
                rs, errors, i, len, records;
            if (errorReader) {
                console.log('errorReader');
                rs = errorReader.read(response);
                records = rs.records;
                errors = [];
                if (records) {
                    for(i = 0, len = records.length; i < len; i++) {
                        errors[i] = records[i].data;
                    }
                }
                if (errors.length < 1) {
                    errors = null;
                }
                return {
                    success : rs.success,
                    errors : errors
                };
            }
            return Ext.decode(response.responseText);
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
            console.log("Exception");
            operation.response = response;
            operation.responseText = response.responseText;
            operation.responseData = Ext.JSON.decode(operation.responseText);
            operation.setException({
                status: response.status,
                statusText: response.statusText
            });
        },
    });



    /***************************
     * FORM
     **************************/
    Ext.create('Ext.form.Panel', {
        title: 'Simple Form',
        renderTo: 'form-ct',
        bodyPadding: 5,
        width: 350,
        model: 'Person',
        errorReader: Ext.create('Ext.data.reader.Json', {
            type : 'json',
            model: 'FieldError',
            root: 'errors',
            successProperty: 'success'
        }),


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
                    waitMsg: 'Saving Data...',
                    success: function(form, action) {
                        console.log(action.response);
                        console.log(action.result);
                    },
                    failure: function(form, action) {
                        // See failureType property in:
                        // http://docs.sencha.com/ext-js/4-0/#/api/Ext.form.action.Submit
                        //console.log('Failure');
                        //console.log(action);
                        console.log(action.result);
                        form.markInvalid(action.result.errors);
                        if (action.failureType === Ext.form.action.Action.CONNECT_FAILURE) {
                            Ext.Msg.alert('Error', 'Connection failed');
                        } else if (action.failureType === Ext.form.action.Action.SERVER_INVALID) {
                            Ext.Msg.alert('Error', action.operation.error.statusText);
                        } else {
                            Ext.Msg.alert('Server ERROR', Ext.String.format("{0}: {1}",
                                action.operation.error.status,
                                action.operation.error.statusText));
                        }
                    }
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
