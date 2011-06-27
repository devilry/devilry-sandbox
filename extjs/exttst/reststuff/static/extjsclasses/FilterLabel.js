Ext.define('devilry.FilterLabel', {
    extend: 'Ext.container.Container',
    style: {
        color: '#FFFFFF',
        backgroundColor:'#000000',
        'border-radius': '6px'
    },
    padding: 5,
    margin: 2,

    constructor: function(filterbox, filter) {
        this.filterbox = filterbox;
        this.filter = filter;
        this.labelText = Ext.String.format('{0}: {1}', filter.property, filter.value);
        this.callParent([{
            layout: 'hbox'
        }]);

        var me = this;
        this.button = Ext.create('Ext.Button', {
            text: 'x',
            listeners: {
                click: function() {
                    me.remove();
                }
            }
        });
        this.add(this.button);

        this.label = Ext.create('Ext.Component', {
            html: this.labelText,
            padding: 5
        });
        this.add(this.label);

        return this;
    },

    remove: function() {
        this.filterbox.removeFilterLabel(this);
    }
});
