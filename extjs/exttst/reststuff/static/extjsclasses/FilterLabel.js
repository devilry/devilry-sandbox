Ext.define('devilry.FilterLabel', {
    extend: 'Ext.container.Container',
    style: {
        color: '#333',
        backgroundColor:'#fff',
        'border-radius': '6px'
    },
    padding: 5,
    margin: 2,
    layout: 'hbox',

    constructor: function(config) {
        this.callParent([config]);

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
            html: Ext.String.format('{0}: {1}', this.filter.property, this.filter.value),
            padding: 5
        });
        this.add(this.label);

        return this;
    },

    remove: function() {
        this.ownerCt.removeFilterLabel(this);
    }
});
