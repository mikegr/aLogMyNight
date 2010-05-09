export BATIK_CMD="java -jar /opt/batik/batik-rasterizer.jar"
export BEER=icon_bier
export ANTI=icon_anti
export COCKTAIL=icon_cocktail
export SHOT=icon_shot
export WINE=icon_wein
export SMALL_SIZE=32
export BIG_SIZE=200
export IMAGE_DIR=images
export DEST_DIR=res/drawable
export DEST_FORMAT=png

#$BATIK_CMD -w 24 -h 24 images/* -d res/drawable-ldpi
#$BATIK_CMD -w 32 -h 32 images/* -d res/drawable-mdpi
#$BATIK_CMD -w 48 -h 48 images/* -d res/drawable-hdpi

$BATIK_CMD -w $SMALL_SIZE -h $SMALL_SIZE $IMAGE_DIR/$ANTI* -d $DEST_DIR/${ANTI}_small.$DEST_FORMAT
$BATIK_CMD -w $SMALL_SIZE -h $SMALL_SIZE $IMAGE_DIR/$BEER* -d $DEST_DIR/${BEER}_small.$DEST_FORMAT
$BATIK_CMD -w $SMALL_SIZE -h $SMALL_SIZE $IMAGE_DIR/$WINE* -d $DEST_DIR/${WINE}_small.$DEST_FORMAT
$BATIK_CMD -w $SMALL_SIZE -h $SMALL_SIZE $IMAGE_DIR/$COCKTAIL* -d $DEST_DIR/${COCKTAIL}_small.$DEST_FORMAT
$BATIK_CMD -w $SMALL_SIZE -h $SMALL_SIZE $IMAGE_DIR/$SHOT* -d $DEST_DIR/${SHOT}_small.$DEST_FORMAT

$BATIK_CMD -w $BIG_SIZE -h $BIG_SIZE $IMAGE_DIR/$ANTI* -d $DEST_DIR/${ANTI}_big.$DEST_FORMAT
$BATIK_CMD -w $BIG_SIZE -h $BIG_SIZE $IMAGE_DIR/$BEER* -d $DEST_DIR/${BEER}_big.$DEST_FORMAT
$BATIK_CMD -w $BIG_SIZE -h $BIG_SIZE $IMAGE_DIR/$WINE* -d $DEST_DIR/${WINE}_big.$DEST_FORMAT
$BATIK_CMD -w $BIG_SIZE -h $BIG_SIZE $IMAGE_DIR/$COCKTAIL* -d $DEST_DIR/${COCKTAIL}_big.$DEST_FORMAT
$BATIK_CMD -w $BIG_SIZE -h $BIG_SIZE $IMAGE_DIR/$SHOT* -d $DEST_DIR/${SHOT}_big.$DEST_FORMAT
