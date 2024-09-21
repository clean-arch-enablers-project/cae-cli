#!/bin/bash

if [ -d "$HOME/cae" ]; then
  echo "Found the cae directory. Using it."
else
  echo "Not found cae directory. Creating it."
  mkdir -p "$HOME/cae"
fi

echo "#!/bin/bash" > "$HOME/cae/cae.sh"
echo "java -jar \$HOME/cae/cae-cli.jar \"\$@\"" >> "$HOME/cae/cae.sh"
chmod +x "$HOME/cae/cae.sh"

export CAE_CLI_HOME="$HOME/cae"
export CAE_META_STRUCTURE_TEMPLATES_PATH="$HOME/cae/file-templates"

if [ -f "$HOME/.zshrc" ]; then
  echo "alias cae='$HOME/cae/cae.sh'" >> "$HOME/.zshrc"
  echo "export CAE_META_STRUCTURE_TEMPLATES_PATH=\"\$CAE_META_STRUCTURE_TEMPLATES_PATH\"" >> "$HOME/.zshrc"
  source "$HOME/.zshrc"
elif [ -f "$HOME/.bash_profile" ]; then
  echo "alias cae='$HOME/cae/cae.sh'" >> "$HOME/.bash_profile"
  echo "export CAE_META_STRUCTURE_TEMPLATES_PATH=\"\$CAE_META_STRUCTURE_TEMPLATES_PATH\"" >> "$HOME/.bash_profile"
  source "$HOME/.bash_profile"
fi

cp ./components/cae-cli.jar "$HOME/cae"
cp -r ./components/file-templates "$HOME/cae/file-templates"

echo "Installation completed. Please restart your terminal with 'source ~/.zshrc' or 'source ~/.bash_profile'."
