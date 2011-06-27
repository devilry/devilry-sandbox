Ext.require(['devilry.FilterBox', 'devilry.FilterSelector']);

Ext.define('devilry.FilterManager', {
    extend: 'Ext.container.Container',
    require: ['devilry.FilterBox'],
    alias: 'widget.devilry-filtermanager', // Registers this as xtype: devilry-filtermanager

    items: [
        {
            xtype: 'devilry-filterselector'
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
