
// Path to the blank image should point to a valid location on your server
Ext.BLANK_IMAGE_URL = '/static/extjs/resources/themes/images/default/tree/s.gif';
Ext.Loader.setConfig({
    enabled:true,
    paths: {
        // Enables us to load any ExtJS class in its own file within this dir
        // (just like in Java) as devilry.path.to.FileName. Example: devily.Person (reststuff/static/extjsclasses/Person.js)
        'devilry': '/static/extjsclasses'
    }
});

Ext.require(['Ext.data.*', 'Ext.grid.*', 'devilry.Person', 'devilry.FilterManager']);


/* Override the method that is used to submit forms. */
Ext.override(Ext.form.action.Submit, {
    run: function() {
        var record = this.form.getRecord();
        if(record) { // Update the current record with data from form if editing existing (previously loaded with loadRecord())
            this.form.updateRecord(record);
        } else { // Create new record
            record = Ext.ModelManager.create(this.form.getValues(), this.form.model);
        }

        // save() automatically uses the correct REST method (post for create and put for update).
        record = record.save({
            form: this.form,
            success: this.onSuccess,
            failure: this.onFailure,
            scope: this,
            timeout: (this.timeout * 1000) || (this.form.timeout * 1000),
        });

        this.form.reset();
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

    // We could override buildRequest and send jsonData instead of params.
    // However this may not be compatible with the GET method specification...?
    // At least it does not seem to end up in request.raw_post_data in Django.
    //buildRequest: function(operation) {
        //var params = Ext.applyIf(operation.params || {}, this.extraParams || {}),
            //request;
        
        ////copy any sorters, filters etc into the params so they can be sent over the wire
        //params = Ext.applyIf(params, this.getParams(params, operation));
        
        //if (operation.id && !params.id) {
            //params.id = operation.id;
        //}
        
        //request = Ext.create('Ext.data.Request', {
            ////params   : params,
            //jsonData: Ext.JSON.encode(params),
            //action   : operation.action,
            //records  : operation.records,
            //operation: operation,
            //url      : operation.url
        //});
        
        //request.url = this.buildUrl(request);
        
        /*
         * Save the request on the Operation. Operations don't usually care about Request and Response data, but in the
         * ServerProxy and any of its subclasses we add both request and response as they may be useful for further processing
         */
        //operation.request = request;
        
        //return request;
    //}
})











function formExample() {
    /***************************
     * FORM
     **************************/
    var form = Ext.create('Ext.form.Panel', {
        title: 'Simple Form',
        renderTo: 'form-example',
        bodyPadding: 5,
        width: 350,
        model: 'devilry.Person',

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

    return form;
}




function editableTableExample(store) {
    var rowEditing = Ext.create('Ext.grid.plugin.RowEditing', {
        errorSummary: true
    });
    
    var grid = Ext.create('Ext.grid.Panel', {
        renderTo: 'table-example',
        plugins: [rowEditing],
        width: 800,
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
            text: 'Email',
            flex: 1,
            sortable: true,
            dataIndex: 'email',
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
                    store.insert(0, new devilry.Person());
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
        }, {
            xtype: 'devilry-filtermanager',
            dock: 'right',
            padding: 10,
            width: 200,
            store: store
        }]
    });
    grid.getSelectionModel().on('selectionchange', function(selModel, selections){
        grid.down('#delete').setDisabled(selections.length === 0); // Disable delete if no items
    });

    return grid;
}




// See http://docs.sencha.com/ext-js/4-0/#/api/Ext.chart.Chart
function chartExample(store) {
    return Ext.create('Ext.chart.Chart', {
       renderTo: 'chart-example',
       width: 400,
       height: 300,
       store: store,

       // Axes only define the rectangles around the chart (try to comment them out, and the series will still draw the bars)
       axes: [
           {
               title: 'User',
               type: 'Category',
               position: 'left',
               fields: ['first'],
           },
           {
               title: 'Score',
               type: 'Numeric',
               position: 'bottom',
               minimum: 0, // If this is not defined, the lowes value we be minimum.
               fields: ['score']
           }
       ],

       // Series define what data to draw in the x and y axis (in this case the series is the bars)
       series: [
           {
               type: 'bar',
               axis: 'bottom',
               xField: 'username',
               yField: ['score'],

               // Extra parameters to make the series prettier
               tips: { // Tooltips on hover
                   trackMouse: true,
                   width: 280,
                   height: 30,
                   renderer: function(storeItem, item) {
                       this.setTitle(Ext.String.format('{0} {1} <{2}>: {3} points',
                           storeItem.get('first'), storeItem.get('last'),
                           storeItem.get('email'), storeItem.get('score')));
                   }
               },
               label: { // Label inside each bar
                   display: 'insideEnd',
                   field: 'score',
                   renderer: Ext.util.Format.numberRenderer('0'),
                   orientation: 'horizontal',
                   color: '#333',
                   'text-anchor': 'middle'
               }
           }
       ],

       // Extra parameters to make the chart prettier
       animate: true
    });
}




/* Select the bar at the selectedIndex in the barSeries. */
function highlightSelection(barSeries, selectedIndex) {
    barSeries.highlight = true;
    barSeries.unHighlightItem();
    barSeries.cleanHighlights();
    barSeries.highlightItem(barSeries.items[selectedIndex]);
    barSeries.highlight = false;
}



Ext.onReady(function(){
    var store = Ext.create('Ext.data.Store', {
        autoLoad: true,
        autoSync: true,
        model: 'devilry.Person',
        remoteFilter: true,
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


    //var filterManager = Ext.create('devilry.FilterManager', {
        //store: store,
        //renderTo: 'filterbox-example'
    //});


    var form = formExample();
    var editableTable = editableTableExample(store);
    var chart = chartExample(store);
    var barSeries = chart.series.get(0);

    editableTable.on('selectionchange', function(selModel, selections){
        var selection = selections[0];
        if(selection) {
            form.loadRecord(selection);
            var selectedIndex = selection.index;
            highlightSelection(barSeries, selectedIndex);
        }
    });

    // Update editableTable and chart after changes to form (this works because they share the same store).
    form.on('actioncomplete', function(formobj, action, options) {
        store.load();
    });

    // Select rows in editableTable when the corresponding item is clicked in the chart.
    barSeries.on('itemmouseup', function(item) {
        var selectedIndex = Ext.Array.indexOf(barSeries.items, item);
        var selectionModel = editableTable.getSelectionModel();
        var selectedStoreItem = item.storeItem;
        editableTable.suspendEvents(); // Suspend events to avoid recursive select
        selectionModel.select(selectedIndex);
        highlightSelection(barSeries, selectedIndex);
        editableTable.resumeEvents();
    });

    store.filter('email', 'duck');
    store.filter('first', 'D');
});
