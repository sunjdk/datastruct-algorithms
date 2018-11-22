/*
 * @(#)BitArrays.java
 */

package ds.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * This class has objects that support bit handling for a large array of bytes.
 */

public class BitArray
{
   // number of bits in the bit array
   private int numberOfBits;

   // number of byte values used for the bit array
   private int byteArraySize;
   // the array itself
   private byte[] member;

	// determine the index of the array element
	// containing bit i
	private int arrayIndex(int i)
	{
		 return i/8;
	}

	// bit i is represented by a bit in member[arrayIndex(i)].
	// return a byte value with a 1 in the
	// position that represents bit i
	private byte bitMask(int i)
	{
		// use & to find the remainder after dividing by
		// 8. remainder 0 puts a 1 in the left-most bit
		// and 7 puts a 1 in the right-most bit
		return (byte)(1 << (7 - (i & 7)));
	}

   // CONSTRUCTORS

	/**
	 * Creates a bit array consisting of <tt>numBits</tt> bits
	 * each having value 0.
	 * @param numBits  number of bits in the bit array.
	 */
	public BitArray(int numBits)
	{
		numberOfBits = numBits;

		// number of bytes's needed to hold
		// numberOfBits elements
		byteArraySize = (numberOfBits+7)/8;

		// initialize the array with all bytes 0
		member = new byte[byteArraySize];
		for (int i=0;i < member.length;i++)
			member[i] = 0;
	}

 	/**
	 * Creates a bit array consisting of bit values from the integer
	 * array <tt>b</tt>. Let n = b.length, the bit array has values
	 * bit 0: b[0], bit 1; b[1], . . . bit n-1: b[n-1].
	 * @param b  integer array that initializes bits in the bit array.
	 */
   public BitArray(int[] b)
   {
      int i;

		numberOfBits = b.length;

      // number of bytes needed to hold
      // numberOfBits elements
      byteArraySize = (numberOfBits+7)/8;

      // initialize the array with all bytes 0
      member = new byte[byteArraySize];
      for (i=0;i < member.length;i++)
         member[i] = 0;

      // set all bits i for which b[i] != 0
      for (i=0; i < numberOfBits; i++)
         if (b[i] != 0)
         	set(i);
	}

   /**
    * ???
    * @param n  ???
    */
    public void assignInt(int n)
   {
		// nullify current reference
		member = null;

      int b;
      // ~0 has value 111...111 in binary.
      // (~0) >>> 1 has value 011...111.
      // ~((~0) >>> 1) has value 100...000.
      // when mask and n are combined with &,
      // all bits but the bit corresponding to the
      // 1 in mask are 0, and the bit corresponding
      // to the 1 in mask retains its value. this
      // allows us to determine if the designated bit
      // is 0 (mask & value == 0) or 1 (mask & value != 0)
      int mask = ~((~0) >>> 1);

      // there are 32 bits in an int
      numberOfBits = 32;
      // the member array needs 4 elements
      byteArraySize = 4;

      // initialize the array with all bytes 0
      member = new byte[byteArraySize];
      for (int i=0;i < member.length;i++)
         member[i] = 0;

      for (b=0;b < numberOfBits;b++)
      {
         // see if the current bit of n is 1. if
         // so, set bit b
         if ((mask & n) != 0 )
            set(b);
         else
            clear(b);
         // move the 1 bit to the right
         mask >>>= 1;
      }
   }

    /**
    * ???
    * @param c  character that ???
    */
  public void assignChar(char c)
   {
		member = null;

      int b;
      // ~0 has value 111...111 in binary.
      // (~0) >>> 1 has value 011...111.
      // ~((~0) >>> 1) has value 100...000.
      // do an unsigned shift of this value
      // 16 bits to the right to obtain
      // 00...01000000000000000.
      // when mask and c are combined with &,
      // all bits but the bit corresponding to the
      // 1 in mask are 0, and the bit corresponding
      // to the 1 in mask retains its value. this
      // allows us to determine if the designated bit
      // is 0 (mask & c == 0) or 1 (mask & c != 0)
      int mask = (~((~0) >>> 1)) >>> 16;

      // there are 16 bits in a char
      numberOfBits = 16;
      // the member array needs 2 elements
      byteArraySize = 2;

      // initialize the array with all bytes 0
      member = new byte[byteArraySize];
      for (int i=0;i < member.length;i++)
         member[i] = 0;

      for (b=0;b < numberOfBits;b++)
      {
         // see if the current bit of c is 1. if
         // so, set bit b
         if ((mask & c) != 0)
            set(b);
         else
            clear(b);
         // move the 1 bit to the right
         mask >>>= 1;
      }
   }

     /**
    * ???
    * @param b  byte that ???
    */
  	public void assignByte(byte b)
   {
		member = null;

      int bit;
      // ~0 has value 111...111 in binary.
      // (~0) >>> 1 has value 011...111.
      // ~((~0) >>> 1) has value 100...000.
      // do an unsigned shift of this value
      // 24 bits to the right to obtain
      // 00...00000000010000000.
      // when mask and c are combined with &,
      // all bits but the bit corresponding to the
      // 1 in mask are 0, and the bit corresponding
      // to the 1 in mask retains its value. this
      // allows us to determine if the designated bit
      // is 0 (mask & c == 0) or 1 (mask & c != 0)
      int mask = (~((~0) >>> 1)) >>> 24;

      // there are 8 bits in a byte
      numberOfBits = 8;
      // the member array needs 1 element
      byteArraySize = 1;

      // initialize the array with all bytes 0
      member = new byte[byteArraySize];
      for (int i=0;i < member.length;i++)
         member[i] = 0;

      for (bit=0;bit < numberOfBits;bit++)
      {
         // see if the current bit of c is 1. if
         // so, set bit b
         if ((mask & b) != 0)
            set(bit);
         else
            clear(bit);
         // move the 1 bit to the right
         mask >>>= 1;
      }
   }

    /**
    * Returns the number of bits in this bit array.
    * @return the number of bits in this bit array
    */
   public int size()
   {
      return numberOfBits;
   }

    /**
    * Returns the value of <tt>bit i</tt> in this bit array.
    * @param i  specified bit location in the bit array.
    * @return the value of <tt>bit i</tt> in this bit array.
    * @throws    IndexOutOfBoundsException if the index is out of range
    *		  (i &lt; 0 || i &gt;= numberOfBits).
    */
	public int bit(int i)
	{
		// is i in range 0 to numberOfBits-1 ?
		if (i < 0 || i >= numberOfBits)
			throw new IndexOutOfBoundsException(
							"BitArray bit(): bit out of range");

		// return the bit corresponding to i
		if ((member[arrayIndex(i)] & bitMask(i)) != 0)
			return 1;
		else
			return 0;
	}

    /**
    * Sets <tt>bit i</tt> in this bit array.
    * @param i  specified bit location in the bit array.
    * @throws    IndexOutOfBoundsException if the index is out of range
    *		  (i &lt; 0 || i &gt;= numberOfBits).
    */
   public void set(int i)
   {
       // is i in range 0 to numberOfBits-1 ?
       if (i < 0 || i >= numberOfBits)
         throw new IndexOutOfBoundsException(
               "BitArray set(): bit out of range");

       // set bit i
       member[arrayIndex(i)] |= bitMask(i);
   }

    /**
    * Clears <tt>bit i</tt> in this bit array.
    * @param i  specified bit location in the bit array.
    * @throws    IndexOutOfBoundsException if the index is out of range
    *		  (i &lt; 0 || i &gt;= numberOfBits).
    */
	public void clear(int i)
	{
		 // is i in range 0 to numberOfBits-1 ?
		 if (i < 0 || i >= numberOfBits)
			throw new IndexOutOfBoundsException(
					"BitArray clear(): bit out of range");

		 // clear the bit corresponding to i. note
		 // that ~bitMask(i) has a 0 in the bit
		 // we are interested in an 1 in all others
		 member[arrayIndex(i)] &= ~bitMask(i);
	}

    /**
    * Clears all of the bits in this bit array.
    */
   public void clear()
   {
      int i;

      for (i=0;i < byteArraySize;i++)
         member[i] = 0;
   }

   // BIT ARRAY OPERATORS

    /**
    * Returns <tt>true</tt> if this bit array is identical to <tt>Object x</tt>.
    * @param x  object that is used for the comparison.
    * @throws    IllegalArgumentException if the two bit arrays do not have
    *		  the same size.
    */
   public boolean equals(Object x)
   {
      BitArray b = (BitArray)x;
      boolean result = true;

      // the bit arrays must have the same size
      if (numberOfBits != b.numberOfBits)
         throw new IllegalArgumentException(
					"BitArray ==: bit arrays are not the same size");

      // compare the member arrays byte by byte
      for(int i=0;i < byteArraySize;i++)
         if (member[i] != b.member[i])
         {
            result = false;
            break;
         }

      return result;
   }

    /**
    * Returns a <tt>BitArray</tt> which is the bitwise <tt>or</tt> between
    * this instance and x.
    * @param x  a <tt>BitArray</tt> that is an operand.
    * @return a <tt>BitArray</tt> which is the bitwise <tt>or</tt> between
    * this instance and x.
    * @throws    IllegalArgumentException if the two bit arrays do not have
    *		  the same size.
    */
	public BitArray or(BitArray x)
	{
		int i;

		// the bit arrays must have the same size
		if (numberOfBits != x.numberOfBits)
			throw
				new IllegalArgumentException(
						"BitArray |: bit arrays are not the same size");

		// form the bitwise OR in tmp
		BitArray tmp = new BitArray(numberOfBits);

		// each member element of tmp is the bitwise
		// OR of the current object and x
		for (i = 0; i < byteArraySize; i++)
			tmp.member[i] = (byte)(member[i] | x.member[i]);

		// return the bitwise OR
		return tmp;
	}

    /**
    * Returns a <tt>BitArray</tt> which is the bitwise <tt>and</tt> between
    * this instance and x.
    * @param x  a <tt>BitArray</tt> that is an operand.
    * @return a <tt>BitArray</tt> which is the bitwise <tt>and</tt> between
    * this instance and x.
    * @throws    IllegalArgumentException if the two bit arrays do not have
    *		  the same size.
    */
   public BitArray and(BitArray x)
   {
      int i;

      // the bit arrays must have the same size
      if (numberOfBits != x.numberOfBits)
         throw
            new IllegalArgumentException(
						"BitArray &: bit arrays are not the same size");

      // form the bitwise AND in tmp
      BitArray tmp = new BitArray(numberOfBits);

      // each member element of tmp is the bitwise
      // AND of the current object and x
      for (i = 0; i < byteArraySize; i++)
         tmp.member[i] = (byte)(member[i] & x.member[i]);

      // return the bitwise AND
      return tmp;
   }

    /**
    * Returns a <tt>BitArray</tt> which is the bitwise <tt>xor</tt> between
    * this instance and x.
    * @param x  a <tt>BitArray</tt> that is an operand.
    * @return a <tt>BitArray</tt> which is the bitwise <tt>xor</tt> between
    * this instance and x.
    * @throws    IllegalArgumentException if the two bit arrays do not have
    *		  the same size.
    */
   public BitArray xor(BitArray x)
   {
      int i;

      // the bit arrays must have the same size
      if (numberOfBits != x.numberOfBits)
         throw
            new IllegalArgumentException(
						"BitArray ^: bit arrays are not the same size");

      // form the bitwise XOR in tmp
      BitArray tmp = new BitArray(numberOfBits);

      // each member element of tmp is the bitwise
      // XOR of the current object and x
      for (i = 0; i < byteArraySize; i++)
         tmp.member[i] = (byte)(member[i] ^ x.member[i]);

      // return the bitwise XOR
      return tmp;
   }


    /**
    * Returns a <tt>BitArray</tt> which is the bitwise <tt>not</tt> complement of
    * this instance.
    * @return a <tt>BitArray</tt> which is the bitwise <tt>not</tt> of this instance.
    */
   public BitArray not()
   {
      // form the bitwise NOT in tmp
      BitArray tmp = new BitArray(numberOfBits);

      // each element of tmp is the bitwise
      // NOT of the current object's member values
      for (int i = 0; i < byteArraySize; i++)
         tmp.member[i] = (byte)~member[i];

      // return the bitwise NOT
      return tmp;
	}

    /**
    * Returns a <tt>BitArray</tt> which is this instance but with the bits
    * shifted to the left n positions.
    * @param n  number of bits to shift left.
    * @return a <tt>BitArray</tt> which is this instance but with the bits
    * shifted to the left n positions..
    */
   public BitArray shiftLeft(int n)
   {
      BitArray tmp = new BitArray(numberOfBits);
      int i, j;
      int prevLeftBit, currLeftBit;
      // ~0 has value 111...111 in binary.
      // (~0) >>> 1 has value 011...111.
      // ~((~0) >>> 1) has value 100...000.
      // do an unsigned shift of this value
      // 24 bits to the right to obtain
      // 00...00000000010000000.
      // when mask and member[i] are combined with &,
      // all bits but the bit corresponding to the
      // 1 in mask are 0, and the bit corresponding
      // to the 1 in mask retains its value
      int mask = (~((~0) >>> 1)) >>> 24;

      // make tmp a copy of the current object
      for (i=0;i < tmp.byteArraySize;i++)
         tmp.member[i] = member[i];

      // shift left one bit a total of n times
      for (i=0;i < n;i++)
      {
         prevLeftBit = 0;
         // cycle through member array right to left
         for (j=tmp.byteArraySize-1;j >= 0;j--)
         {
            // extract the left bit of member[j] and move it
            // to the right 7 bits. it will become
            // the right bit of member[j-1]
            currLeftBit = (tmp.member[j] & mask) >> 7;
            // shift left
            tmp.member[j] <<= 1;
            // add left bit of member[j+1] in right bit
            // of member[j]
            tmp.member[j] |= prevLeftBit;
            // prepare for next iteration
            prevLeftBit = currLeftBit;
         }
      }

      return tmp;
   }

    /**
    * Returns a <tt>BitArray</tt> which is this instance but with the bits
    * shifted ??? to the right n positions.
    * @param n  number of bits to shift right.
    * @return a <tt>BitArray</tt> which is this instance but with the bits
    * shifted ??? to the right n positions..
    */
   public BitArray shiftSignedRight(int n)
   {
      BitArray tmp = new BitArray(numberOfBits);
      int i, j;
      int prevRightBit, currRightBit;
      // used to isolate the right-most bit of a
      // member entry
      int mask = 1, maskbit7 = 127;

      // make tmp a copy of the current object
      for (i=0;i < tmp.byteArraySize;i++)
         tmp.member[i] = member[i];

      // shift right one bit a total of n times
      for (i=0;i < n;i++)
      {
         prevRightBit = 0;
         // cycle through member array left to right
         for (j=0;j < tmp.byteArraySize;j++)
         {
            // extract right bit of member[j]. it will become
            // the left bit of member[j+1]
            currRightBit = tmp.member[j] & mask;

            if (j == 0)
               // do signed shift right
               tmp.member[j] >>= 1;
            else
               // do unsigned shift right for j >= 1
               tmp.member[j] = (byte)((tmp.member[j] >>> 1) & maskbit7);

            // add right bit of member[j-1] in left bit
            // of member[j]
            tmp.member[j] |= (prevRightBit << 7);

            // prepare for next iteration
            prevRightBit = currRightBit;
         }
      }

      return tmp;
   }

    /**
    * Returns a <tt>BitArray</tt> which is this instance but with the bits
    * shifted ??? to the right n positions.
    * @param n  number of bits to shift right.
    * @return a <tt>BitArray</tt> which is this instance but with the bits
    * shifted ??? to the right n positions..
    */
   public BitArray shiftUnsignedRight(int n)
   {
      BitArray tmp = new BitArray(numberOfBits);
      int i, j;
      int prevRightBit, currRightBit;
      // used to isolate the right-most bit of a
      // member entry
      int mask = 1, maskbit7 = 127;


      // make tmp a copy of the current object
      for (i=0;i < tmp.byteArraySize;i++)
         tmp.member[i] = member[i];

      // shift right one bit a total of n times
      for (i=0;i < n;i++)
      {
         prevRightBit = 0;
         // cycle through member array left to right
         for (j=0;j < tmp.byteArraySize;j++)
         {
            // extract right bit of member[j]. it will become
            // the left bit of member[j+1]
            currRightBit = tmp.member[j] & mask;

            // do unsigned shift right
            tmp.member[j] = (byte)((tmp.member[j] >>> 1) & maskbit7);

            // add right bit of member[j-1] in left bit
            // of member[j]
            tmp.member[j] |= (prevRightBit << 7);

            // prepare for next iteration
            prevRightBit = currRightBit;
         }
      }

      return tmp;
   }

    /**
    * Write the bit array as a sequence of bytes to a <tt>DataOutputStream</tt>.
    * @param ostr  <tt>DataOutputStream</tt> that stores the bit array.
    */
   // output the bit array to the binary file stream ostr
   public void write(DataOutputStream ostr) throws IOException
   {
      try
      {
			ostr.write(member,0,byteArraySize);
		}
		catch (IOException ioe)
		{
			throw new IOException("BitArray write(): I/O error");
		}
   }

     /**
    * Read the numBits bit array from a <tt>DataInputStream</tt>.
    * @param istr  the <tt>DataInputStream</tt> source file.
    */
   public void read(DataInputStream istr, int numBits)
   	throws IllegalArgumentException, IOException
   {
		member = null;

		// reassign numberOfBits
		numberOfBits = numBits;

		// number of bytes needed to hold numBits
		// elements
		byteArraySize = (numberOfBits+7)/8;

		member = new byte[byteArraySize];

		try
		{
			if (istr.read(member) != byteArraySize)
					throw
						new IllegalArgumentException(
								"BitArray read(): end of file reached " +
								"before reading required number of bits");
		}
		catch(IOException ioe)
		{
			throw new IOException("BitArray read(): I/O error");
		}
   }

   /**
    * Returns a string representation of bit array.
    * @return a string representation of bit array.
    */
   // output the bit array as a string
   public String toString()
   {
		String s = "";

		for (int b = 0; b < numberOfBits; b++)
			if (bit(b) != 0)
				s += "1";
			else
				s += "0";

		return s;
   }
}
