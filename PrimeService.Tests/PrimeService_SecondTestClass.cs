using System;
using Xunit;

namespace Prime.UnitTests.Services
{
    public class PrimeService_SecondTestClass
    {
        [Fact]   
        public void PassingTest()
        {
            Assert.Equal(4, Decimal.Add(2, 2));
        }
        [Fact]
        public void FailingTest()
        {
            Assert.Equal(5, Decimal.Add(2, 2));
        }
        
    }
}
