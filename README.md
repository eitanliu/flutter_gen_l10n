# Flutter Gen-L10n

## How to use

1. GitHub [Releases](https://github.com/eitanliu/flutter_gen_l10n/releases) or IntelliJ IDEs Plugin Marketplace [Flutter Gen-L10n](https://plugins.jetbrains.com/plugin/21845) page download plugin
2. Open in the IDE `Setting` -> `Plugins`, click setting icon ⚙️ -> `Install Plugin from Disk...` selected you download file.

<!-- Plugin description -->
Support for extracting strings to internationalization arb files and generating Flutter localizations.

### l10n.yaml
```yaml
# The directory where the template and translated arb files are located.  
# (defaults to "lib/l10n")
arb-dir: lib/l10n
# The template arb file that will be used as the basis for generating the Dart localization and messages files.
# (defaults to "app_en.arb")
template-arb-file: app_en.arb
nullable-getter: false
# Determines whether or not the generated output files will be generated as a synthetic package or at a specified directory in the Flutter project.
# (defaults is "true")
# synthetic-package: false
# The directory where the generated localization classes will be written if the synthetic-package flag is set to false.
# (defaults to ".dart_tool/flutter_gen/gen_l10n")
# output-dir: lib/generated
# The Dart class name to use for the output localization and localizations delegate classes.
# (defaults to "AppLocalizations")
output-class: AppLocalizations
# The filename for the output localization and localizations delegate classes.
# (defaults to "app_localizations.dart")
output-localization-file: app_localizations.dart
```
<!-- Plugin description end -->