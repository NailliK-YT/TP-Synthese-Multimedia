import re
import sys
from pathlib import Path

def remove_non_javadoc_comments(content):
    """
    Supprime tous les commentaires sauf les JavaDoc.
    Garde les commentaires /** ... */ et supprime // et /* ... */
    """
    lines = content.split('\n')
    result = []
    in_javadoc = False
    in_block_comment = False
    
    i = 0
    while i < len(lines):
        line = lines[i]
        stripped = line.lstrip()
        
        # Vérifier si on entre dans un JavaDoc
        if '/**' in line and not in_javadoc:
            in_javadoc = True
            result.append(line)
            if '*/' in line:
                in_javadoc = False
            i += 1
            continue
        
        # Si on est dans un JavaDoc, garder la ligne
        if in_javadoc:
            result.append(line)
            if '*/' in line:
                in_javadoc = False
            i += 1
            continue
        
        # Vérifier si on entre dans un commentaire de bloc non-JavaDoc
        if '/*' in line and '/**' not in line and not in_block_comment:
            in_block_comment = True
            # Supprimer la ligne ou la partie commentaire
            if '*/' in line:
                # Commentaire sur une seule ligne, supprimer
                in_block_comment = False
                i += 1
                continue
            else:
                i += 1
                continue
        
        # Si on est dans un commentaire de bloc, ignorer
        if in_block_comment:
            if '*/' in line:
                in_block_comment = False
            i += 1
            continue
        
        # Supprimer les commentaires de ligne //
        if '//' in line:
            # Vérifier si le // est dans une chaîne
            in_string = False
            quote_char = None
            comment_pos = -1
            
            for j, char in enumerate(line):
                if char in ('"', "'") and (j == 0 or line[j-1] != '\\'):
                    if not in_string:
                        in_string = True
                        quote_char = char
                    elif char == quote_char:
                        in_string = False
                        quote_char = None
                elif char == '/' and j+1 < len(line) and line[j+1] == '/' and not in_string:
                    comment_pos = j
                    break
            
            if comment_pos >= 0:
                # Garder seulement la partie avant le commentaire
                line_before = line[:comment_pos].rstrip()
                if line_before:
                    result.append(line_before)
                i += 1
                continue
        
        # Supprimer les lignes vides multiples (garder maximum 1 ligne vide)
        if not stripped:
            if result and result[-1].strip():
                result.append(line)
        else:
            result.append(line)
        
        i += 1
    
    # Nettoyer les lignes vides en trop à la fin
    while result and not result[-1].strip():
        result.pop()
    
    return '\n'.join(result) + '\n'

def clean_java_file(file_path):
    """Nettoie un fichier Java de ses commentaires non-JavaDoc"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        cleaned = remove_non_javadoc_comments(content)
        
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(cleaned)
        
        print(f"✓ {file_path}")
        return True
    except Exception as e:
        print(f"✗ {file_path}: {e}")
        return False

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage: python clean_comments.py <file1.java> [file2.java ...]")
        sys.exit(1)
    
    success_count = 0
    for file_path in sys.argv[1:]:
        if clean_java_file(file_path):
            success_count += 1
    
    print(f"\nNettoyé {success_count}/{len(sys.argv)-1} fichiers")
