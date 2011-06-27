Ext.define('devilry.FilterManager', {
    extend: 'Ext.container.Container',
    require: ['devilry.FilterBox'],
    //renderTo: 'filterbox-example',
    width: 250,

    constructor: function(store, config) {
        this.callParent([config]);
        this.filterbox = Ext.create('devilry.FilterBox', store);
        this.clearAllButton = Ext.create('Ext.Button', {
            text: 'Clear all filters',
            listeners: {
                click: function() {
                    store.filters.clear();
                }
            }
        });

        var me = this;
        this.addfield = Ext.create('Ext.form.field.Text', {
            fieldLabel: 'Add filter',
            width: me.width,
            listeners: {
                specialkey: function(textfield, e) {
                    if(e.getKey() == e.ENTER) {
                        store.filter('first', textfield.getValue());
                        textfield.setValue('');
                    }
                }
            }
        });

        this.add(this.addfield);
        this.add(this.filterbox);
        this.add(this.clearAllButton);
        return this;
    }
});
