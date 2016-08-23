package clg.gesclg;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class datos extends AppCompatActivity {

    private final String ruta_fotos = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString(); //+ "/misfotos/";
    private File file = new File(ruta_fotos);
    private Button botonHacerFoto;
    private Button botonArchivoFoto;
    private ImageView imagenHacerFoto;
    private ImageView imagenArchivoFoto;
    private Bitmap bmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //======== codigo hacer foto ========

        botonHacerFoto = (Button) findViewById(R.id.buttonHacerFoto);
        //Si no existe crea la carpeta donde se guardaran las fotos
        file.mkdirs();
        //accion para el botonHacerFoto
        botonHacerFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagenHacerFoto = (ImageView) findViewById(R.id.imageViewImagenCamara);
                String file = ruta_fotos + getCode() + ".jpg";
                File mi_foto = new File(file);
                try {
                    mi_foto.createNewFile();
                } catch (IOException ex) {
                    Log.e("ERROR ", "Error:" + ex);
                }

                Uri uri = Uri.fromFile(mi_foto);
                //Abre la camara para tomar la foto
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                //Retorna a la actividad
                startActivityForResult(cameraIntent, 0);

                //Guarda imagenHacerFoto
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            }
        });

        //======== codigo archivo foto ========

        botonArchivoFoto = (Button)findViewById(R.id.buttonCargarFoto);

        botonArchivoFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                imagenArchivoFoto = (ImageView)findViewById(R.id.imageViewImagenArchivo);
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent.createChooser(intent, "Selecciona imagen siniestro"), 100);
                //imagenArchivoFoto.setImageURI(intent.getData());
                imagenArchivoFoto.setMaxHeight(10);
                imagenArchivoFoto.setMaxWidth(10);
            }});

        //====== codigo nuevo:end ======


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

    }

    /**
     * Metodo privado que genera un codigo unico segun la hora y fecha del sistema
     * @return photoCode
     * */
    @SuppressLint("SimpleDateFormat")
    private String getCode()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date() );
        String photoCode = "pic_" + date;
        return photoCode;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                Bundle ext = data.getExtras();
                bmp = (Bitmap)ext.get("data");
                imagenHacerFoto.setImageBitmap(bmp);
            }
        }
        else
        {
            if(resultCode == Activity.RESULT_OK) {
                Uri path = data.getData();
                imagenArchivoFoto.setImageURI(path);

            }
        }
    }

    /**
     * Función que permite redimensionar una imagen para cargarla en un imageview.
     * @param ctx
     * @param resId
     * @param w
     * @param h
     * @return
     */
    public static Drawable resizeImage(Context ctx, int resId, int w, int h) {

        // cargamos la imagen de origen
        Bitmap BitmapOrg = BitmapFactory.decodeResource(ctx.getResources(),
                resId);

        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        // calculamos el escalado de la imagen destino
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // para poder manipular la imagen
        // debemos crear una matriz

        Matrix matrix = new Matrix();
        // resize the Bitmap
        matrix.postScale(scaleWidth, scaleHeight);

        // volvemos a crear la imagen con los nuevos valores
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0,
                width, height, matrix, true);

        // si queremos poder mostrar nuestra imagen tenemos que crear un
        // objeto drawable y así asignarlo a un botón, imageview...
        return new BitmapDrawable(resizedBitmap);

    }
}

