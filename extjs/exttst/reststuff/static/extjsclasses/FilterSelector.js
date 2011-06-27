Ext.define('devilry.FilterSelector', {
    extend: 'Ext.container.Container',
    layout: 'hbox',
    height: 30,
    alias: 'widget.devilry-filterselector', // Registers this as xtype: devilry-filterselector

    items: [
        {
            xtype: 'textfield',
            emptyText: 'Add filter...',
            width: 100,
            listeners: {
                specialkey: function(textfield, e) {
                    if(e.getKey() == e.ENTER) {
                        this.ownerCt.addFilter();
                    }
                }
            }
        }, {
            xtype: 'button',
            text: '+',
            listeners: {
                click: function() {
                    this.ownerCt.addFilter();
                }
            }
        }
    ],

    afterRender: function() {
        this.combo = Ext.create('Ext.form.field.ComboBox', {
            store: Ext.create('Ext.data.Store', {
                fields: ['property', 'label'],
                data: this.ownerCt.propertyData
            }),
            queryMode: 'local',
            displayField: 'label',
            valueField: 'property',
            forceSelection: true,
            editable: false,
            value: this.ownerCt.defaultProperty,
            width: 60
        });
        this.insert(0, this.combo);

        return this;
    },

    addFilter: function() {
        var textfield = this.items.items[1];
        var value = textfield.getValue();
        if(value != '') {
            this.ownerCt.store.filter(this.combo.getValue(), value);
            textfield.setValue('');
        }
    }
});
