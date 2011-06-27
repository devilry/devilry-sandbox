Ext.require(['devilry.FilterLabel']);
Ext.define('devilry.FilterBox', {
    extend: 'Ext.container.Container',
    require: ['devilry.FilterLabel'],
    alias: 'widget.devilry-filterbox', // Registers this as xtype: devilry-filterbox

    constructor: function(config) {
        this.callParent([config]);
        return this;
    },

    /** Called when the component has been rendered. */
    afterRender: function(){
        this.callParent();

        this.store = this.ownerCt.store;
        var me = this;

        // Add event listeners for remove, add and clear to ensure that we
        // keep the list in sync with the filters
        this.store.filters.on('remove', function(filter, key) {
            Ext.each(me.items.items, function(filterlabel) {
                if(filterlabel.filter === filter) {
                    me.remove(filterlabel);
                }
            });
            me.store.load();
        });

        this.store.filters.on('add', function(index, filter) {
            me.addFilter(filter);
            me.store.load();
        });

        this.store.filters.on('clear', function() {
            me.removeAll();
            me.store.load();
        });

        this.store.filters.on('replace', function() {
            me.refresh();
            me.store.load();
        });

        this.refresh();
    },

    /* Refresh from filters */
    refresh: function() {
        this.removeAll();
        var me = this;
        Ext.each(me.store.filters.items, function(filter, index, allFilters) {
            me.addFilter(filter);
        });
    },

    addFilter: function(filter) {
        var filterlabel = Ext.create('devilry.FilterLabel', {
            filter: filter
        });
        this.add(filterlabel);
    },

    removeFilterLabel: function(filterlabel) {
        this.store.filters.remove(filterlabel.filter);
    }
});
