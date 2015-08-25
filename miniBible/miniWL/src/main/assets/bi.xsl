<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="html" indent="no"/>
	<xsl:param name="chap"/>
	<xsl:param name="verse"/>
	<xsl:param name="style"/>
	<xsl:template match="/">
		<html>
			<head>
				<style>
					<xsl:value-of select="$style" disable-output-escaping="yes"/>
					div {width:100%}
					a {vertical-align:top}
					.dc {color:grey}
					.dv {color:grey;font-size:8pt;vertical-align:top}
				</style>
			</head>
			<body>
				<div id = "content">
					<xsl:copy-of select="//text[@chap=$chap]"/>
				</div>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>