from pathlib import Path
import re,json,hashlib,shutil
root=Path.cwd(); archive=root/'tache2-rapports/final-integration/etape-03'; before=archive/'before';before.mkdir(parents=True,exist_ok=True)
if (archive/"decisions.json").exists():
 raise SystemExit("Archives existantes : utiliser une copie vierge du commit Saidana pour rejouer, sans ecraser les preuves.")
# Java tokens: braces inside strings, characters and comments are not structural.
tokens=re.compile(r'"(?:\\.|[^"\\])*"|\'(?:\\.|[^\'\\])*\'|//[^\n]*|/\*[\s\S]*?\*/|[{}]')
pattern=re.compile(r'(?m)^[ \t]*(?:@[^\n]+\n[ \t]*)*public void (\w+)\([^\n]*\)[^{]*\{')
def methods(s):
 for m in pattern.finditer(s):
  if '@Test' not in m.group():continue
  depth=1
  for t in tokens.finditer(s,m.end()):
   if t.group()=='{':depth+=1
   elif t.group()=='}':depth-=1
   if depth==0:
    yield m.start(),t.end(),m.group(1),s[m.start():t.end()];break
fix={}
def add(file,meth,old,new,why):fix[(file+'.java',meth)]=(old,new,why)
add('EndianUtils_getUIntBE_27_0_Test','testGetUIntBEWithLargeNumber','4294967295L','0x00FFFFFFL','BE non signe : 00 FF FF FF = 16777215.')
add('EndianUtils_getUIntBE_27_0_Test','testGetUIntBEWithOffset','assertEquals(1L, result)','assertEquals(0L, result)','Les quatre octets a offset 4 sont nuls.')
add('EndianUtils_getUIntLE_24_0_Test','testGetUIntLE','assertEquals(1, result)','assertEquals(0L, result)','Les quatre premiers octets sont nuls ; les octets suivants ne sont pas lus.')
add('EndianUtils_getUIntLE_24_0_Test','testGetUIntLEWithOffset','assertEquals(1, result)','assertEquals(0x00010000L, result)','LE a offset 4 : 00 00 01 00 = 65536.')
add('EndianUtils_getUShortBE_18_0_Test','testGetUShortBEWithOffset','assertEquals(514, result)','assertEquals(0x0203, result)','BE a offset 1 : 02 03 = 515.')
for meth,value in [('testGetUShortBE',1),('testGetUShortBEWithOffset',0)]:add('EndianUtils_getUShortBE_19_0_Test',meth,'int expected = 256;','int expected = '+str(value)+';','BE : les deux octets lus sont 00 01 ou 00 00.')
for meth,old,new in [('testGetUShortLE','assertEquals(1, result)','assertEquals(0x0100, result)'),('testGetUShortLEWithOffset','assertEquals(258, result)','assertEquals(0x0201, result)')]:add('EndianUtils_getUShortLE_14_0_Test',meth,old,new,'LE : octet de poids faible en premier ; retirer aussi le catch qui masque les exceptions.')
add('EndianUtils_readIntBE_7_1_Test','testReadIntBEWithNegativeInput','assertThrows(BufferUnderrunException.class, () -> EndianUtils.readIntBE(inputStream));','assertEquals(0xFF000000, EndianUtils.readIntBE(inputStream));','ByteArrayInputStream.read renvoie 255 pour le byte -1 ; quatre octets disponibles, entier BE signe.')
add('EndianUtils_readIntLE_6_1_Test','testReadIntLENegativeInput','Assertions.assertThrows(BufferUnderrunException.class, () -> endianUtils.readIntLE(inputStream));','Assertions.assertEquals(-1, endianUtils.readIntLE(inputStream));','Quatre octets FF donnent -1 en entier signe ; ils ne representent pas EOF.')
add('EndianUtils_readLongBE_10_1_Test','testReadLongBE','0xFFFFFFFFFFFFFFFEL','0xFFFEFDFCFBFAF9F8L','Assemblage BE des huit octets FF FE FD FC FB FA F9 F8, sans changer les donnees.')
add('EndianUtils_readLongLE_9_1_Test','testReadLongLE','0xFEDCBA9876543210L','0xF0DEBC9A78563412L','Assemblage LE des huit octets 12 34 56 78 9A BC DE F0.')
add('EndianUtils_readShortBE_1_1_Test','testReadShortBE','assertThrows(BufferUnderrunException.class, () -> {\n            endianUtils.readShortBE(inputStream);\n        });','assertEquals((short) 0x1234, endianUtils.readShortBE(inputStream));','Deux octets suffisent pour un short BE ; le -1 apres les deux octets nest pas lu.')
add('EndianUtils_readShortLE_0_1_Test','testReadShortLEWithNegativeBytes','assertThrows(BufferUnderrunException.class, () -> EndianUtils.readShortLE(inputStream));','assertEquals((short) 0xFEFF, EndianUtils.readShortLE(inputStream));','Bytes FF FE disponibles : short LE signe -257, sans underrun.')
add('EndianUtils_readUE7_11_0_Test','testReadUE7_MultipleBytes','assertEquals(129, result)','assertEquals(128L, result)','Format UE7 : (1 << 7) + 0 = 128.')
add('EndianUtils_readUE7_11_0_Test','testReadUE7_MaxValue','assertEquals(0x7FFFFFFFFFFFFFFFL, result)','assertEquals(127L, result)','Premier 7F sans bit de continuation : arret apres le premier octet. Renommer le cas trompeur.')
for meth,ext in [('testCalculateExtension_withContentType_png','png'),('testCalculateExtension_withContentType_pdf','pdf'),('testCalculateExtension_withContentType_ocr_png','png')]:add('FilenameUtils_calculateExtension_10_1_Test',meth,'assertEquals("'+ext+'", result)','assertEquals(".'+ext+'", result)','Le registre MIME renvoie une extension avec son point, comme les tests originaux.')
for meth,old,new in [('testGetNameWithParentDirectory','StringUtils.EMPTY','"report.pdf"'),('testGetNameWithCurrentDirectory','StringUtils.EMPTY','"report.pdf"'),('testGetNameWithReservedCharacters','"report.pdf"','"report?pdf"'),('testGetNameWithASCIINumeric','StringUtils.EMPTY','".abcde"')]:add('FilenameUtils_getName_1_0_Test',meth,'assertEquals('+old+', result)','assertEquals('+new+', result)','getName garde le dernier segment ; il ne normalise ni ? ni une extension seule.')
add('MediaType_image_2_0_Test','testGetBaseType','MediaType.parse("image")','MediaType.parse("image/png")','getBaseType retire les parametres, pas le sous-type.')
add('MediaType_parse_7_0_Test','testParseInvalidType','assertNull(MediaType.parse("invalid/type"));','assertEquals("invalid/type", MediaType.parse("invalid/type").toString());','Le type est syntaxiquement valide ; parse ne verifie pas un registre de noms. Renommer le cas.')
rows=[]
files=sorted(p for p in (root/'tika-core/src/test/java').rglob('*_Test.java') if not p.name.startswith('._') and p.name.startswith(('EndianUtils_','MediaType_','FilenameUtils_')))
for p in files:
 s=p.read_text(); rel=p.relative_to(root); dest=before/(str(rel)+'.txt');dest.parent.mkdir(parents=True,exist_ok=True);dest.write_bytes(p.read_bytes())
 changes=[]
 for start,end,name,block in methods(s):
  old=block;disabled='@Disabled' in block;key=(p.name,name);decision='accepted_unchanged';why='Cas actif herite, assertions conservees ; execution a verifier.'
  if key in fix:
   a,b,why=fix[key];assert a in block,key;block=block.replace(a,b);decision='corrected'
   if p.name=='EndianUtils_getUShortLE_14_0_Test.java':
    block=block.replace('        try {\n','').replace('        } catch (Exception e) {\n            e.printStackTrace();\n        }','')
  elif disabled:
   why='Candidat rejete : oracle non justifie sur le domaine valide ; ne pas figer un comportement douteux ou garder un test artificiel.'
   if p.name.startswith('EndianUtils_ubyteToInt'):why='byte primitif jamais null : exception attendue impossible, cas repetitifs sans intention valide.'
   elif p.name in ['EndianUtils_getIntBE_23_0_Test.java','EndianUtils_getShortBE_17_1_Test.java']:why='Reflexion inutile, erreurs de valeurs ou dexceptions enveloppees ; couverture assuree par autres tests directs.'
   elif p.name.startswith('MediaType_audio') or p.name.startswith('MediaType_text'):why='Plusieurs attentes dexceptions ou de normalisation ne decoulent pas du contrat ; reparation substantielle, fabriques couvertes manuellement.'
   elif 'EscapedQuotes' in name:why='Syntaxe de guillemets echappes et attente ambigues ; conserver lechec pour analyse plutot que figer un comportement non garanti.'
   elif 'WithNullMetadata' in name:why='Metadata null hors du contrat documente ; ne pas remplacer arbitrairement une valeur attendue par une exception.'
   elif 'ReservedCharacters' in name:why='Oracle depend de la plateforme et attend une sortie du repertoire pour un nom lexical interieur.'
   elif 'InvalidPath' in name:why='invalid/path est un chemin relatif valide, test redondant avec withValidPath ; le mot invalid ne prouve aucune invalidite.'
   elif p.name.startswith('FilenameUtils_getSuffix'):why='Methode multi-scenarios avec plusieurs oracles faux ; cas de suffixes deja couverts par les tests originaux et manuels.'
   elif 'hashCode' in p.name:why='Montage Mockito inutilise ; oracle valable mais cas deja couvert par le test manuel hashCode.'
   elif 'getShortLE_12' in p.name or 'getUIntBE_26' in p.name:why='Montage de flux sans rapport avec une lecture de tableau ; cas nominal deja couvert par autres tests directs.'
   if '@DisabledOnOs' in block:
    if name=='testResolveWithinOutside':
     block=re.sub(r'(?m)^\s*@DisabledOnOs[^\n]*\n','\n',block)
     block=block.replace('Paths.get("/tmp")','temporary.resolve("inside")')
     block=block.replace('public void testResolveWithinOutside()','public void testResolveWithinOutside(@org.junit.jupiter.api.io.TempDir Path temporary)')
     block=block.replace('"../etc/passwd"','"../outside.txt"')
     block=block.replace('assertEquals("\'../etc/passwd\' resolves to \'/etc/passwd\', which is outside of \'/tmp\'", exception.getMessage());','assertEquals("\'../outside.txt\' resolves to \'" + temporary.resolve("outside.txt") + "\', which is outside of \'" + dir + "\'", exception.getMessage());')
     decision='corrected';why='Meme intention de rejet de .. ; TempDir et message exact construit avec Path pour les separateurs du systeme.'
    else:decision='rejected';why='Doublon exact du cas Outside ; aucun lien symbolique nest cree.'
   else:decision='rejected'
   if decision=='rejected':block=''
  if key == ("EndianUtils_readIntBE_7_1_Test.java", "testReadIntBEWithNegativeInput") and block:
   block=block.replace("throws IOException", "throws IOException, BufferUnderrunException")
  if key == ("EndianUtils_readShortLE_0_1_Test.java", "testReadShortLEWithNegativeBytes") and block:
   block=block.replace("throws IOException", "throws IOException, BufferUnderrunException")
  if block:
   block=re.sub(r'(?m)^\s*@Disabled\([^\n]*\)\s*\n','\n',block)
   if name=='testReadUE7_MaxValue':block=block.replace(name,'testReadUE7StopsAtFirstByteWithoutContinuation')
   if name=='testParseInvalidType':block=block.replace(name,'testParseSyntacticallyValidUnregisteredType')
   # Reject an empty test instead of inventing an assertion attributed to the model.
   body=block[block.index('{')+1:block.rfind('}')]
   body=re.sub(r'//[^\n]*|/\*[\s\S]*?\*/','',body)
   if not body.strip():block='';decision='rejected';why='Corps vide : aucun comportement ni oracle ; ne pas compter comme test utile.'
  rows.append({'file':str(rel),'method':name,'decision':decision,'reason':why,'before_sha256':hashlib.sha256(old.encode()).hexdigest(),'before':old,'after':block})
  if block!=old:changes.append((start,end,block))
 for a,b,value in reversed(changes):s=s[:a]+value+s[b:]
 if changes:
  s=re.sub(r'(?m)^// .*desactive[^\n]*\n','',s)
  s=s.replace('// Exporte tel quel par ChatUniTest (compilait sans intervention).','// Compilait avant integration ; selection/corrections humaines tracees dans etape-03.')
  s=s.replace('// Toutes les methodes de test reussissent sur le code non mute.','// Resultat historique ; voir la validation finale de etape-03.')
  s=s.replace('import org.junit.jupiter.api.condition.DisabledOnOs;\n','').replace('import org.junit.jupiter.api.condition.OS;\n','')
  s=s.replace('import org.junit.jupiter.api.Disabled;\n','')
  if any(r['file']==str(rel) and r['decision']!='rejected' for r in rows):p.write_text(s)
  else:p.unlink()
(archive/'decisions.json').write_text(json.dumps(rows,indent=2,ensure_ascii=False)+'\n')
print(len(rows),'cas inventoriés')
from collections import Counter
print(Counter(r['decision'] for r in rows))
