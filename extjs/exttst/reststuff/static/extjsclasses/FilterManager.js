Ext.require(['devilry.FilterBox']);

Ext.define('devilry.FilterManager', {
    extend: 'Ext.container.Container',
    require: ['devilry.FilterBox'],
    alias: 'widget.devilry-filtermanager', // Registers this as xtype: devilry-filtermanager

    items: [
        {
            xtype: 'textfield',
            emptyText: 'Add filter...',
            listeners: {
                specialkey: function(textfield, e) {
                    if(e.getKey() == e.ENTER) {
                        this.ownerCt.store.filter('first', textfield.getValue());
                        textfield.setValue('');
                    }
                }
            }
        }, {
            xtype: 'devilry-filterbox'
        }, {
            xtype: 'button',
            text: 'Clear all filters',
            listeners: {
                click: function() {
                    this.ownerCt.store.filters.clear();
                }
            }
        }
    ],

    constructor: function(config) {
        this.callParent([config]);
        return this;
    }
});
