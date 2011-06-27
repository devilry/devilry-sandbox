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
                        this.ownerCt.ownerCt.store.filter(this.ownerCt.combo.getValue(), textfield.getValue());
                        textfield.setValue('');
                    }
                }
            }
        }
    ],

    afterRender: function() {

        var me = this;
        var data = [
            {"property":"first", "label":"First"},
            {"property":"last", "label":"Last"},
            {"property":"email", "label":"Email"}
        ];
        this.combo = Ext.create('Ext.form.field.ComboBox', {
            store: Ext.create('Ext.data.Store', {
                fields: ['property', 'label'],
                data: data
            }),
            queryMode: 'local',
            displayField: 'label',
            valueField: 'property',
            forceSelection: true,
            editable: false,
            value: data[2].property,
            width: 70
        });
        this.insert(0, this.combo);

        return this;
    },
});
