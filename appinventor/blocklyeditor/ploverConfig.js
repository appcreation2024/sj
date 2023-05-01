{
  // "This config file is used to compile the blockly code base."
  "id": "blockly-config",
  "paths": [],
  "inputs": [//'testalert.js'
    "../lib/blockly/blockly_compressed.js",
    // Lyn's instrumentation code
    "./src/instrument.js",

    //finally, include any of our own .js file in any order
    "./src/msg.js",
    "./src/events.js",
    "./src/blocklyeditor.js",
    './src/typeblock.js',
    "./src/blockly.js",
    "./src/events.js",
    "./src/block.js",
    "./src/workspace.js",
    "./src/xml.js",
    "./src/trashcan.js",
    "./src/scrollbar.js",
    "./src/block_svg.js",
    "./src/connection_db.js",
    "./src/component_database.js",
    "./src/procedure_database.js",
    "./src/variable_database.js",
    "./src/workspace_svg.js",
    "./src/blockColors.js",
    "./src/translation_properties.js",
    "./src/translation_events.js",
    "./src/translation_methods.js",
    "./src/translation_params.js",
    "./src/drawer.js",
    "./src/savefile.js",
    "./src/versioning.js",
    "./src/mutators.js",
    "./src/field_lexical_variable.js",
    "./src/errorIcon.js",
    "./src/warningHandler.js",
    "./src/field_procedure.js",
    "./src/field_textblockinput.js",
    "./src/field_invalid_dropdown.js",
    "./src/warningIndicator.js",
    "./src/exportBlocksImage.js",
    "./src/flydown.js",
    "./src/field_flydown.js",
    "./src/field_parameter_flydown.js",
    "./src/field_global_flydown.js",
    "./src/field_procedurename.js",
    "./src/nameSet.js",
    "./src/substitution.js",
    "./src/warning.js",
    "./src/toolboxController.js",
    "./src/field.js",
    "./src/rendered_connection.js",
    "./src/input.js",
    "./src/field_event_flydown.js",
    "./src/utils_xml.js",

    // Dialog Utiltiy
    "./src/util.js",

    // backpack files
    "./src/backpack.js",
    "./src/backpackFlyout.js",

    //blocks files
    './src/blocks/control.js',
    './src/blocks/logic.js',
    './src/blocks/text.js',
    './src/blocks/lists.js',
    './src/blocks/math.js',
    './src/blocks/utilities.js',
    './src/blocks/procedures.js',
    './src/blocks/lexical-variables.js',
    './src/blocks/colors.js',
    './src/blocks/components.js',
    './src/blocks/dictionaries.js',
    './src/blocks/helpers.js',

    //generator files
    "./src/generators/yail.js",
    "./src/generators/yail/componentblock.js",
    "./src/generators/yail/lists.js",
    "./src/generators/yail/math.js",
    "./src/generators/yail/control.js",
    "./src/generators/yail/logic.js",
    "./src/generators/yail/text.js",
    "./src/generators/yail/colors.js",
    "./src/generators/yail/variables.js",
    "./src/generators/yail/procedures.js",
    "./src/generators/yail/dictionaries.js",
    './src/generators/yail/helpers.js',
    './src/generators/swift.js',

    // Repl
    "./src/replmgr.js",

    // extras
    "./src/extras.js"
    ],

  // This must be specified because datetimesymbols.js from the Closure Library
  // will be included, so when test-raw.html loads each input in RAW mode,
  // it is important that the proper charset be used.
  "output-charset": "UTF-8",
  "mode": "SIMPLE",
  "experimental-compiler-options": {
    "languageIn": "ECMASCRIPT5"
  },
  "dependency_mode": "PRUNE_LEGACY",
  "closure-library" : "../lib/closure-library/closure/goog",
  "output-file": "../build/blocklyeditor/blockly-all.js"
}
